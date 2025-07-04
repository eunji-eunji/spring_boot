package com.example.demo.repository;

import com.example.demo.entity.Product;
import com.example.demo.entity.ReviewPost;
import com.example.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewPostRepository extends JpaRepository<ReviewPost, Long> {
    // 리뷰 게시글 작성 여부 확인
    boolean existsByUserAndProduct(Users user, Product product);
}
