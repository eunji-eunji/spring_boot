package com.example.demo.controller;


import com.example.demo.dto.ProductDetailDto;
import com.example.demo.dto.ProductForm;
import com.example.demo.entity.Product;
import com.example.demo.entity.Users;
import com.example.demo.service.ProductService;
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


    @GetMapping("/admin/products")
    public String showProductForm(Model model) {
        model.addAttribute("productForm", new ProductForm()); // 초기 빈 폼 생성
        return "product/products"; // templates/product/products.html
    }
    // 관리자만 접근 가능, 이미지 업로드
    @PostMapping("/admin/products")
    public String createProduct(@ModelAttribute ProductForm form, @RequestParam("images") List<MultipartFile> images) {
        try {
            productService.createProduct(form, images);
        } catch (Exception e) {
            // 예외 처리 로직 (ex. 에러 페이지로 리다이렉트)
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

    // 상품 상세보기, 후기, 찜기능
    @GetMapping("/products/{id}")
    public String viewProduct(@PathVariable Long id, Model model, @AuthenticationPrincipal Users user) {
        ProductDetailDto dto = productService.getProductDetail(id, user);
        model.addAttribute("product", dto);
        return "product/detail";
    }



}
