package example.boardapp.controller;

import example.boardapp.dto.BoardDto;
import example.boardapp.entity.Board;
import example.boardapp.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/boards")
public class BoardController {
    @Autowired
    private BoardService boardService;

    @GetMapping("/new")
    public String createForm(Model model){
        model.addAttribute("boardDto", new BoardDto());
        return "boards/create";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute BoardDto dto){
        boardService.create(dto);
        return "redirect:/boards";
    }

    @GetMapping
    public String list(Model model){
        List<Board> boards=boardService.findAll();
        model.addAttribute("boards", boards);
        return "boards/list";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model){
        Board board = boardService.findById(id);
        if(board == null) return "redirect:/boards";
        // entity -> dto
        BoardDto dto =new BoardDto(board);
        model.addAttribute("boardDto", dto);
        return "boards/edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id, @ModelAttribute BoardDto dto) {
        boardService.update(id, dto);
        return "redirect:/boards";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id){
        boardService.delete(id);
        return "redirect:/boards";
    }
}
