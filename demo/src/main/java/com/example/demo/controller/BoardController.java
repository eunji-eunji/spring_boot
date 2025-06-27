package com.example.demo.controller;

import com.example.demo.domain.Board;
import com.example.demo.service.BoardJpaService;
import com.example.demo.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/boards")
public class BoardController {
    @Autowired
    private BoardJpaService boardJpaService;

    @GetMapping
    public String list(Model model){
        List<Board> boards=boardJpaService.findAll();
        model.addAttribute("board", boards);
        return "board/list";
    }

    @GetMapping("/new")
    public String form(){
        return "board/form";
    }

    @PostMapping
    public String create(@ModelAttribute Board board){
        boardJpaService.save(board);
        return "redirect:/boards";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model){
        Board board=boardJpaService.findById(id);
        model.addAttribute("board", board);
        return "board/detail";
    }
}
