package com.example.demo.repository;


import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 카테고리로 상품 목록 조회
    List<Product> findByCategory(Category category);

    // 선택적으로 소분류 ID로 조회
    List<Product> findByCategory_Id(Long categoryId);

    List<Product> findByCategoryIdIn(List<Long> categoryIds);

//    @Query("SELECT p FROM Product p WHERE p.category.id IN :ids")
//    List<Product> findByCategoryIdIn(@Param("ids") List<Long> ids);

}
