package loginBoard.board2.controller;

import jakarta.servlet.http.HttpSession;
import loginBoard.board2.dto.BoardDTO;
import loginBoard.board2.entity.Board;
import loginBoard.board2.entity.Comment;
import loginBoard.board2.entity.Member;
import loginBoard.board2.service.BoardService;
import loginBoard.board2.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/boards/write")
    public String writeForm(HttpSession session){
        if(session.getAttribute("loginUser") == null){
            return "redirect:/login";
        }
        return  "board-write";
    }
    @PostMapping("/boards/write")
    public String write(BoardDTO dto, HttpSession session){
       Member writer = (Member) session.getAttribute("loginUser");

       if(writer == null) return  "redirect:/login";
        Board abc = boardService.create(dto, writer);
        System.out.println(abc.toString());
        return "redirect:/boards";

    }
    @GetMapping("boards")
    public String list(Model model , HttpSession session){
        Member loginUser = (Member) session.getAttribute("loginUser");
        model.addAttribute("loginUser", loginUser);
        List<Board> boards= boardService.list();
        model.addAttribute("boards", boards);
        Map<Long , List<Comment>> commentMap = new HashMap<>();
        for(Board board : boards){
            commentMap.put(board.getId(),commentService.getComments(board) );
        }
        model.addAttribute("commentMap", commentMap);
        return "board-list";

    }
    @PostMapping("/comments/add")
    public String addComment(@RequestParam Long boardId,
                             @RequestParam String content,
                             HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        commentService.saveComment(boardId, content, loginUser);
        return "redirect:/boards";
    }

    @GetMapping("/boards/edit/{id}")
    public String editform(@PathVariable Long id, HttpSession session, Model model){
        Member loginUser = (Member) session.getAttribute("loginUser");
        if(loginUser == null) return "redirect:/login";
        Board board = boardService.findById(id);
        if(!board.getWriter().getId().equals(loginUser.getId())){
            return "redirect:/boards?error=unauthorized";
        }
        model.addAttribute("board",board);
        return "board-edit";
    }
    @PostMapping("/boards/edit/{id}")
    public String edit(@PathVariable Long id, HttpSession session, BoardDTO dto){
        Member loginUser = (Member) session.getAttribute("loginUser");
        if(loginUser == null) return "redirect:/login";
        Board board = boardService.findById(id);
        if(!board.getWriter().getId().equals(loginUser.getId())){
            return "redirect:/boards?error=unauthorized";
        }
        boardService.update(id, dto);
        return "redirect:/boards";
    }

    @GetMapping("/boards/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session){
        Member loginUser = (Member) session.getAttribute("loginUser");
        if(loginUser == null) return "redirect:/login";
        Board board = boardService.findById(id);
        if(!board.getWriter().getId().equals(loginUser.getId())){
            return "redirect:/boards?error=unauthorized";
        }

        boardService.delete(id);
        return "redirect:/boards";
    }



}
