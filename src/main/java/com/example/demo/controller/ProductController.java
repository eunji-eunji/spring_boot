package com.example.demo.controller;


import com.example.demo.dto.ProductDetailDto;
import com.example.demo.dto.ProductForm;
import com.example.demo.entity.Product;
import com.example.demo.entity.Users;
import com.example.demo.service.ProductService;
import com.example.demo.service.WishlistService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final WishlistService wishlistService;

    @GetMapping("/admin/products")
    public String showProductForm(Model model) {
        model.addAttribute("productForm", new ProductForm()); // 초기 빈 폼 생성
        return "product/products"; // templates/product/products.html
    }
    // 관리자만 접근 가능, 이미지 업로드
    @PostMapping("/admin/products")
    public String createProduct(@ModelAttribute ProductForm form,
                                @RequestParam("images") List<MultipartFile> images,
                                HttpSession session) {
        try {
            Users loginUser = (Users) session.getAttribute("loginUser"); // ✅ 세션에서 로그인 유저 가져오기
            productService.createProduct(form, images, loginUser);       // ✅ 작성자 정보 전달
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
        return "redirect:/products";
    }


    // 카테고리/가격대/정렬 등 필터링 지원
    @GetMapping("/products")
    public String listProducts(@RequestParam(required = false) String category, Model model) {
        List<Product> products = productService.findProducts(category);
        model.addAttribute("products", products);
        return "product/list";
    }

    // 상품 상세보기, 후기
//    @GetMapping("/products/{id}") // Security
//    public String viewProduct(@PathVariable Long id, Model model, @AuthenticationPrincipal Users user) {
//        ProductDetailDto dto = productService.getProductDetail(id, user);
//        model.addAttribute("product", dto);
//        return "product/detail";
//    }

    @GetMapping("/products/{id}") // Session 방식
    public String viewProduct(@PathVariable Long id, Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("loginUser");  // 🔁 세션에서 유저 정보 꺼냄

        ProductDetailDto dto = productService.getProductDetail(id, user);
        model.addAttribute("product", dto);
        model.addAttribute("loginUser", user); // 🟡 view에서 접근할 수 있도록 추가
        return "product/detail";
    }

    // 상품 찜기능 보류
    @PostMapping("/wishlist/toggle")
    public String toggleWishlist(@RequestParam Long productId, HttpSession session) {
        Users user = (Users) session.getAttribute("loginUser");

        if (user == null) {
            return "redirect:/user/login";
        }

        wishlistService.toggleWishlist(user, productId);

        return "redirect:/products/" + productId;
        // TODO: 로그인 연동 완료 시 @AuthenticationPrincipal Users user 로 변경
    }

    // 상품 수정 폼 이동 // 시큐리티 버젼
//    @GetMapping("/products/{id}/edit")
//    public String editProductForm(@PathVariable Long id,
//                                  @AuthenticationPrincipal Users user,
//                                  Model model) {
//        Product product = productService.findById(id);
//
//        if (!product.getUser().getId().equals(user.getId())) {
//            return "redirect:/access-denied"; // 또는 에러 페이지
//        }
//
//        model.addAttribute("productForm", ProductForm.from(product)); // 수정 폼용 DTO로 변환
//        return "product/update"; // 수정 form HTML
//    }

    // 세션방식
    @GetMapping("/products/{id}/edit")
    public String editProductForm(@PathVariable Long id,
                                  HttpSession session,
                                  Model model) {

        Users loginUser = (Users) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/user/login"; // 로그인 안 됐을 경우 로그인 페이지로 리다이렉트
        }

        Product product = productService.findById(id);

        // 작성자 본인만 접근 허용
        if (product.getUser() == null || !product.getUser().getId().equals(loginUser.getId())) {
            return "redirect:/access-denied"; // 또는 사용자 정의 에러 페이지
        }

        model.addAttribute("productForm", ProductForm.from(product)); // 수정 폼용 DTO로 변환
        return "product/update";
    }

    // 상품 수정 처리 시큐리티 방식
//    @PostMapping("/products/{id}/edit")
//    public String updateProduct(@PathVariable Long id,
//                                @ModelAttribute ProductForm form,
//                                @AuthenticationPrincipal Users user) {
//        productService.updateProduct(id, form, user);
//        return "redirect:/products/" + id;
//    }

    @PostMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute ProductForm form,
                                HttpSession session) {

        Users loginUser = (Users) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/user/login"; // 로그인 안 된 경우 로그인 페이지로 이동
        }

        productService.updateProduct(id, form, loginUser);
        return "redirect:/products/" + id;
    }



    // 상품 삭제 처리 // 시큐리티용
//    @PostMapping("/products/{id}/delete")
//    public String deleteProduct(@PathVariable Long id,
//                                @AuthenticationPrincipal Users user) throws Exception {
//        productService.deleteProduct(id, user);
//        return "redirect:/products";
//    }

   // 세션방식 상품 삭제 처리
    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id, HttpSession session) throws Exception {
        Users loginUser = (Users) session.getAttribute("loginUser");
        productService.deleteProduct(id, loginUser);
        return "redirect:/products";
    }







}
