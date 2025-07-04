package com.example.demo.dto;


import com.example.demo.entity.Product;
import com.example.demo.entity.Review;
import com.example.demo.entity.Users;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDetailDto {

    private Product product;
    private List<Review> reviews;
    private boolean liked;

    public ProductDetailDto(Product product, boolean liked) {
        this.product = product;
        this.liked = liked;
    }

    public String getName() {
        return product.getName();
    }

    public int getPrice() {
        return product.getPrice();
    }

    public String getDescription() {
        return product.getDescription();
    }

    public List<?> getImages() {
        return product.getImages();
    }

    public List<?> getProductModels() {
        return product.getProductModels();
    }

    public Long getId() {
        return product.getId();
    }

    public Users getUser() {
        return product.getUser();
    }




}