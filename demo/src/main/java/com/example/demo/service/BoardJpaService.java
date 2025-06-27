package com.example.demo.service;

import com.example.demo.domain.Board;
import com.example.demo.repository.BoardJpaRepository;
import com.example.demo.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardJpaService {
    @Autowired
    private BoardJpaRepository boardJpaRepository;

    @Transactional
    public void save(Board board) {
        boardJpaRepository.save(board);
    }

    public Board findById(Long id) {
        return boardJpaRepository.findById(id).orElse(null);
    }

    public List<Board> findAll() {
        return boardJpaRepository.findAll();
    }

    public void delete(Board board) {
        boardJpaRepository.delete(board);
    }
}
