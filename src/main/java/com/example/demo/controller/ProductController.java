package com.example.demo.controller;


import com.example.demo.dto.ProductDetailDto;
import com.example.demo.dto.ProductForm;
import com.example.demo.entity.Product;
import com.example.demo.entity.Users;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.ProductService;
import com.example.demo.service.WishlistService;
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
        model.addAttribute("productForm", new ProductForm());
        return "product/products";
    }

    @PostMapping("/admin/products")
    public String createProduct(@ModelAttribute ProductForm form,
                                @RequestParam("images") List<MultipartFile> images,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        try {
            Users loginUser = customUserDetails.getUser();
            productService.createProduct(form, images, loginUser);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String listProducts(@RequestParam(required = false) String category, Model model) {
        List<Product> products = productService.findProducts(category);
        model.addAttribute("products", products);
        return "product/list";
    }

    @GetMapping("/products/{id}")
    public String viewProduct(@PathVariable Long id,
                              Model model,
                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Users user = customUserDetails != null ? customUserDetails.getUser() : null;
        ProductDetailDto dto = productService.getProductDetail(id, user);
        model.addAttribute("product", dto);
        model.addAttribute("loginUser", user);
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
                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Users loginUser = customUserDetails != null ? customUserDetails.getUser() : null;
        if (loginUser == null) {
            return "redirect:/user/login";
        }
        productService.updateProduct(id, form, loginUser);
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
