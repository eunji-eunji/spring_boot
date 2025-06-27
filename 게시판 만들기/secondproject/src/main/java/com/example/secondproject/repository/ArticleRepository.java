package com.example.secondproject.repository;

import com.example.secondproject.entity.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {
    ArrayList<Article> findAll();
}
