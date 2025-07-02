package com.example.demo.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

public class Review { // 후기(리뷰)
    @Id @GeneratedValue
    private Long id;

    private String content;
    private int rating; // 점수

    @ManyToOne
    private Users user;

    @ManyToOne
    private Product product;

    private LocalDateTime createdAt;
}
