package com.example.firstproject.repository;

import com.example.firstproject.entity.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

// JpaRepository : 정렬 가능
public interface ArticleRepository extends CrudRepository<Article, Long> {
    ArrayList<Article> findAll();
}
