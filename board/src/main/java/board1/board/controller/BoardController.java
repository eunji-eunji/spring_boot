package board1.board.controller;

import board1.board.entity.Board;
import board1.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/board")
/*공통적으로 앞에 /board 를 넣겠다*/
public class BoardController {
    @Autowired
    private BoardService boardService;
    /*서비스 객체 하나만 만들어짐.*/

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("board", new Board());
        return "form";
    }

    @PostMapping("/save")
    public String save(Board board){
        boardService.save(board);
        return "redirect:/board";
    }

    @GetMapping
    public String list(Model model){
        model.addAttribute("boardList", boardService.findAll());
        return "list";
    }
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model){
        Board board=boardService.findById(id).orElseThrow();
        model.addAttribute("board", board);
        return "detail";
    }
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model){
        Board board=boardService.findById(id).orElseThrow();
        model.addAttribute("board", board);
        return "edit";
    }
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute Board board){
        Board existing=boardService.findById(id).orElseThrow();
        existing.setTitle(board.getTitle());
        existing.setContent(board.getContent());
        boardService.save(existing);
        return "redirect:/board/"+id;
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        boardService.delete(id);
        return "redirect:/board";
    }
}
