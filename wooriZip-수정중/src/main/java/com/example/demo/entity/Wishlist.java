package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Wishlist { // 찜
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Users user;

    @ManyToOne
    private Product product;
}
