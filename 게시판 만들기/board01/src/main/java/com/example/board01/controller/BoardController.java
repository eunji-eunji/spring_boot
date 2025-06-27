package com.example.board01.controller;

import com.example.board01.dto.BoardDTO;
import com.example.board01.service.BoardService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("board", new BoardDTO());
        return "form";
    }
    //생성자 방식에서는 dto에 getter, setter가 필요
    @PostMapping("boards")
    public String create(@ModelAttribute BoardDTO dto){
        boardService.save(dto);
        return "redirect:/boards";
    }

    @GetMapping("/boards")
    public String list(Model model){
        model.addAttribute("boards", boardService.findAll());
        return "boards";
    }
@GetMapping("/boards/{id}")
public  String detail(@PathVariable Long id, Model model){
        model.addAttribute("board",boardService.findById(id));
        return "detail";
}

@GetMapping("/boards/edit/{id}")
public  String editForm(@PathVariable Long id, Model model){
        model.addAttribute("board",boardService.findById(id));
        return "edit-form";
}
    @PostMapping("/boards/edit/{id}")
    public  String edit(@PathVariable Long id, @ModelAttribute BoardDTO dto){
        boardService.update(id, dto);
        return "redirect:/boards";
    }
    @GetMapping("/boards/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/boards";
    }

}
