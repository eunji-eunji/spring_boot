package springboot.test.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springboot.test.domain.Board;
import springboot.test.domain.BoardFormDto;
import springboot.test.domain.Member;
import springboot.test.service.BoardService;
import java.io.IOException;

@Controller
@RequestMapping(value = "/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    // 목록
    @GetMapping("/list")
    public String viewHomePage(Model model) {
        model.addAttribute("boards", boardService.list());
        return "board/list";
    }

    // 글쓰기 폼
    @GetMapping("/write")
    public String post() {
        return "board/write";
    }

    @PostMapping("/write")
    public String submitBoard(BoardFormDto boardDto, @AuthenticationPrincipal User user) throws IOException {
        String loginId = user.getUsername(); // 로그인 아이디 가져오기
        boardService.create(boardDto, loginId);
        return "redirect:/board/list";
    }

    // 게시글 조회 페이지 (읽기 전용)
    @GetMapping("/view/{id}")
    public String viewPost(@PathVariable Long id, HttpServletRequest request, Model model) {
        Board board = boardService.findById(id);
        model.addAttribute("boardDto", board);

        HttpSession session = request.getSession(true);
        Member member = (Member) session.getAttribute("userLoginInfo");

        model.addAttribute("buttonOk", false);
        if (member != null && board.getWriterid().equals(member.getMemberId())) {
            model.addAttribute("buttonOk", true);
        }
        return "board/view";
    }

    // 게시글 수정 폼
    @GetMapping("/update/{id}")
    public String editPostForm(@PathVariable Long id, HttpServletRequest request, Model model) {
        Board board = boardService.findById(id);
        model.addAttribute("boardDto", board);

        HttpSession session = request.getSession(true);
        Member member = (Member) session.getAttribute("userLoginInfo");

        // 권한 확인: 본인 글만 수정 가능
        if (member == null || !board.getWriterid().equals(member.getMemberId())) {
            return "redirect:/board/list";  // 권한 없으면 목록으로
        }

        return "board/edit";
    }

    // 게시글 수정 처리
    @PostMapping("/update/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute BoardFormDto boardDto) throws IOException {
        boardService.update(id, boardDto);
        return "redirect:/board/view/" + id;
    }


    // 게시글 삭제
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id, HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Member member = (Member) session.getAttribute("userLoginInfo");
        Board board = boardService.findById(id);

        if (member != null && board.getWriterid().equals(member.getMemberId())) {
            boardService.deleteBoardById(id);
        }

        return "redirect:/board/list";
    }
}
