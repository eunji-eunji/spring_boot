package com.example.demo.dto;


import com.example.demo.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductForm {
    private String name;
    private String description;
    private int price;
    private String category;
    private int stockQuantity;

    //  하위 모델들
    private List<ProductModelDto> productModelDtoList = new ArrayList<>();

    // 엔티티 변환
    public Product createProduct() {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setAverageRating(0.0); // 평점 기본값
        product.setStockQuantity(0);   // 이후에 모델들 합산
        return product;
    }
}
