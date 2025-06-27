package loginBoard.board2.controller;

import jakarta.servlet.http.HttpSession;
import loginBoard.board2.entity.Board;
import loginBoard.board2.entity.Member;
import loginBoard.board2.service.BoardService;
import loginBoard.board2.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MypageController {
    @Autowired
    private BoardService boardService;

    @Autowired
    private MemberService memberService;

    @GetMapping("/mypage")
    public String mypage(Model model, HttpSession session){
        Member loginUser = (Member) session.getAttribute("loginUser");
        if(loginUser == null) return "redirect:/login";

        // 작성한 게시글 목록
        List<Board> myBoards = boardService.findByWriter(loginUser);
        model.addAttribute("myBoards", myBoards);
        return "mypage";
    }

    @PostMapping("/mypage/password")
    public String changePassword(@RequestParam String newPassword, HttpSession session){
        Member loginUser = (Member) session.getAttribute("loginUser");
        if(loginUser == null) return "redirect:/login";
        memberService.changePassword(loginUser.getId(), newPassword);
        return "redirect:/mypage?success=password";
    }
}
