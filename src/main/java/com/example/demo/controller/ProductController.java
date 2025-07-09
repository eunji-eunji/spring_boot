package com.example.demo.controller;

import com.example.demo.dto.ProductDetailDto;
import com.example.demo.dto.ProductForm;
import com.example.demo.dto.ProductModelDto;
import com.example.demo.dto.ReviewPostDto;
import com.example.demo.entity.*;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.ProductService;
import com.example.demo.service.ReviewPostService;
import com.example.demo.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final WishlistService wishlistService;
    private final ReviewPostService reviewPostService;
    private final CategoryRepository categoryRepository;

    @GetMapping("/admin/products")
    public String showProductForm(Model model) {
        model.addAttribute("productForm", new ProductForm());
        return "product/products";
    }

    // 상품등록
    @PostMapping("/admin/products")
    public String createProduct(@ModelAttribute ProductForm form,
                                @RequestParam("images") MultipartFile[] images,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        try {
            Users loginUser = customUserDetails.getUser();
            productService.createProduct(form, Arrays.asList(images), loginUser);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
        return "redirect:/products";
    }




    @GetMapping("/products")
    public String showProductList(@RequestParam(name = "category", required = false) Long categoryId, Model model) {
        List<Product> productList = productService.findProducts(categoryId);
        model.addAttribute("products", productList);
        return "product/list"; // 실제 Thymeleaf 템플릿 경로에 맞게 조정
    }

    // ✅ ProductController.java
    @GetMapping("/products/{id}")
    public String viewProduct(@PathVariable Long id,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "latest") String sort,
                              Model model,
                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Users user = customUserDetails != null ? customUserDetails.getUser() : null;

        // 상품 정보
        ProductDetailDto dto = productService.getProductDetail(id, user);
        model.addAttribute("product", dto);
        model.addAttribute("loginUser", user);

        // 리뷰 페이지네이션 + 정렬
        int pageSize = 5;
        Page<ReviewPostDto> reviewPage = reviewPostService.findPagedByProductSorted(id, page, pageSize, sort);

        model.addAttribute("reviews", reviewPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reviewPage.getTotalPages());
        model.addAttribute("sort", sort);

        // 리뷰 통계 (평균 별점, 개수, 점수 분포)
        model.addAttribute("reviewCount", reviewPostService.getReviewCount(id));
        model.addAttribute("averageRating", reviewPostService.getAverageRating(id));
        model.addAttribute("ratingCounts", reviewPostService.getRatingDistribution(id));

        // 리뷰 작성 여부 & 로그인 상태
        boolean hasWrittenReview = (user != null) && reviewPostService.hasUserReviewedProduct(user, id);
        model.addAttribute("hasWrittenReview", hasWrittenReview);
        model.addAttribute("isLoggedIn", user != null);

        return "product/detail";
    }




    @PostMapping("/wishlist/toggle")
    public String toggleWishlist(@RequestParam Long productId,
                                 @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Users user = customUserDetails != null ? customUserDetails.getUser() : null;
        if (user == null) {
            return "redirect:/user/login";
        }
        wishlistService.toggleWishlist(user, productId);
        return "redirect:/products/" + productId;
    }

    @GetMapping("/products/{id}/edit")
    public String editProductForm(@PathVariable Long id,
                                  @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                  Model model) {
        Users loginUser = customUserDetails != null ? customUserDetails.getUser() : null;
        if (loginUser == null) {
            return "redirect:/user/login";
        }
        Product product = productService.findById(id);
        if (product.getUser() == null || !product.getUser().getId().equals(loginUser.getId())) {
            return "redirect:/access-denied";
        }
        model.addAttribute("productForm", ProductForm.from(product));
        return "product/update";
    }

    @PostMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute ProductForm form,
                                @RequestParam(value = "images", required = false) MultipartFile[] images,
                                @RequestParam(value = "deleteIndexes", required = false) String deleteIndexes,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Users loginUser = customUserDetails.getUser();
        productService.updateProduct(id, form, images, deleteIndexes, loginUser);
        return "redirect:/products/" + id;
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails) throws Exception {
        Users loginUser = customUserDetails != null ? customUserDetails.getUser() : null;
        if (loginUser == null) {
            return "redirect:/user/login";
        }
        productService.deleteProduct(id, loginUser);
        return "redirect:/products";
    }
}
