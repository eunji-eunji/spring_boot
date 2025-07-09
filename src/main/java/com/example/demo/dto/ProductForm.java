package com.example.demo.dto;


import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductImage;
import com.example.demo.entity.Users;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductForm {
    private Long id;
    private String name;
    private String description;
    private int price;
    private String category;
    private int stockQuantity;

    private Long categoryId;

    //  하위 모델들
    private List<ProductModelDto> productModelDtoList = new ArrayList<>();
    private List<String> imageUrls;

    // 엔티티 변환
    public Product createProduct(Category category, Users user) {
        Product product = new Product();
        product.setName(this.name);
        product.setDescription(this.description);
        product.setPrice(this.price);
        product.setCategory(category); // 엔티티 기준
        product.setUser(user);
        product.setAverageRating(0.0);
        product.setStockQuantity(0);
        return product;
    }

    public static ProductForm from(Product product) {
        ProductForm form = new ProductForm();
        form.setId(product.getId());
        form.setName(product.getName());
        form.setDescription(product.getDescription());
        form.setPrice(product.getPrice());

        // 모델 정보 매핑 (옵션)
        if (product.getProductModels() != null && !product.getProductModels().isEmpty()) {
            List<ProductModelDto> modelDtoList = product.getProductModels().stream()
                    .map(model -> {
                        ProductModelDto dto = new ProductModelDto();
                        dto.setProductModelSelect(model.getProductModelSelect());
                        dto.setPrStock(model.getPrStock());
                        dto.setImageUrl(model.getImageUrl());
                        return dto;
                    })
                    .toList();
            form.setProductModelDtoList(modelDtoList);
        }

        // ✅ 이미지 정보 매핑
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            List<String> imageUrls = product.getImages().stream()
                    .map(ProductImage::getImageUrl)
                    .toList();
            form.setImageUrls(imageUrls);
        }

        return form;
    }

}
