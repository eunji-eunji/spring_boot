package com.example.demo.repository;

import com.example.demo.entity.InteriorPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InteriorPostRepository extends JpaRepository<InteriorPost, Long> {
    List<InteriorPost> findAllByOrderByCreatedAtDesc();
    Page<InteriorPost> findAll(Pageable pageable);
}
