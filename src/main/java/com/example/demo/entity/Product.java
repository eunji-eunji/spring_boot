package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Product { // 상품
    @Id @GeneratedValue
    private Long id;

    private String name;
    private String description;
    private int price;

    private String category; // 예: 가구, 침대, 조명 등

    private int stockQuantity; // 전체 재고 수량
    private double averageRating; // 후기 평점 평균

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductModel> productModels = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,  orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<ProductImage> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    public void addProductModel(ProductModel model) {
        this.productModels.add(model);
        model.setProduct(this);
    }

    public String getThumbnailUrl() {
        if (images != null && !images.isEmpty()) {
            return images.get(0).getImageUrl();
        }
        return null;
    }
}
