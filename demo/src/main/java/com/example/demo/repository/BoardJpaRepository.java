package com.example.demo.repository;

import com.example.demo.domain.Board;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BoardJpaRepository extends CrudRepository<Board, Long> {
    List<Board> findAll();
}
