package com.example.demo.entity;


import com.example.demo.constant.ProductModelSelect;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProductModel {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProductModelSelect productModelSelect;

    private int prStock;

    private String imageUrl;

    @ManyToOne
    private Product product;
}
