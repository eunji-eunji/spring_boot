package board3.board.controller;


import board3.board.dto.BoardDTO;
import board3.board.entity.Member;
import board3.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;                 // 목록 화면에서 사용
import org.springframework.web.bind.annotation.*; // GetMapping, PostMapping, ModelAttribute 등 포함
import org.springframework.web.multipart.MultipartFile; // 이미지 업로드 시 필요
import java.io.IOException;

import java.io.IOException;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/boards/write")
    public String writeForm() {
        return "board-write";
    }

    @PostMapping("/boards/write")
    public String write(@ModelAttribute BoardDTO dto, HttpSession session) throws IOException {
        Member writer = (Member) session.getAttribute("loginUser");
        boardService.create(dto, writer);
        return "redirect:/boards";
    }

    @GetMapping("/boards")
    public String list(Model model){
        model.addAttribute("boards", boardService.list());
        return "board-list";
    }

    @GetMapping("/boards/edit/{id}")
    public String editForm(@PathVariable Long id, Model model){
        model.addAttribute("board", boardService.findById(id));
        return "board-edit";
    }

    @PostMapping("/boards/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute BoardDTO dto) throws IOException{
        boardService.update(id, dto);
        return "redirect:/boards";
    }

    @GetMapping("/boards/delete/{id}")
    public String delete(@PathVariable Long id){
        boardService.delete(id);
        return "redirect:/boards";
    }

}
