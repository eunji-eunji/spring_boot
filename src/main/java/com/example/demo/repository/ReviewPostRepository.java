package com.example.demo.repository;

import com.example.demo.entity.Product;
import com.example.demo.entity.ReviewPost;
import com.example.demo.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewPostRepository extends JpaRepository<ReviewPost, Long> {
    // 리뷰 게시글 작성 여부 확인
    boolean existsByUserAndProduct(Users user, Product product);

    List<ReviewPost> findByProductId(Long productId);

    //  페이지네이션
    Page<ReviewPost> findByProductId(Long productId, Pageable pageable);

    // 총 개수 카운트용
    long countByProductId(Long productId);
}
