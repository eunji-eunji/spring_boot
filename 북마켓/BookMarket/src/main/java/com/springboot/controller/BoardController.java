package com.springboot.controller;

import com.springboot.domain.Board;
import com.springboot.domain.BoardFormDto;
import com.springboot.domain.Member;
import com.springboot.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequestMapping(value = "/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/list")
    public String viewHomePage(Model model){
        return viewPage(1, "id","desc", model);
    }

    @GetMapping("page")
    public String viewPage(@RequestParam("pageNum") int pageNum,
                           @RequestParam("sortField") String sortField,
                           @RequestParam("sortDir") String sortDir,
                           Model model){
        Page<Board> page = boardService.listAll(pageNum, sortField, sortDir);
        //페이징 + 정렬된 데이터를 가져오기

        List<Board> listBoard = page.getContent();
        //현재 페이지에 해당하는 게시글 리스트 꺼내기

        //뷰에 필요한 정보 모델 담기
        model.addAttribute("currentPage", pageNum);//현재페이지
        model.addAttribute("totalPages", page.getTotalPages());//전체 페이지
        model.addAttribute("totalItems", page.getTotalElements());//전체 게시글 수

        model.addAttribute("sortField", sortField); //현재 정렬 기준 컬럼
        model.addAttribute("sortDir", sortDir); // 현재 정렬 방향
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        //정렬 토글버튼 만들때 유용 (제목 정렬 버튼  asc -> desc 전환)
        model.addAttribute("boardList", listBoard);
        //실제 게시글 리스트 전달
        return "board/list";

    }

    @GetMapping("/write")
    public String post(){
        return "board/write";
    }
    //게시글 저장
    @PostMapping("/write")
    public String write(BoardFormDto boardDto) {
        boardService.savePost(boardDto);
        return "redirect:/board/list";
    }

    @GetMapping("/view/{id}")
    public String requestUpdateMemberForm(@PathVariable(name="id") Long id,
                                          HttpServletRequest httpServletRequest,
                                          Model model){
        Board board= boardService.getBoardById(id); //게시글 조회
        model.addAttribute("boardFormDto", board);

        //로그인 사용자 확인 및 권한 판단.
        HttpSession session = httpServletRequest.getSession(true);
        Member member = (Member) session.getAttribute("userLoginInfo");
        //세션에서 로그인된 사용자 정보를 꺼냄 - userLoginInfo

        model.addAttribute("buttonOk",false);
        //수정, 삭제버튼 비활성화 - 기본값
        if(member !=null && board.getWriterid().equals(member.getMemberId())){
            model.addAttribute("buttonOk", true);
        }
        //글쓴사람과 로그인한 사람이 같으면 수정,삭제 버튼 표시
        return "board/view";
    }

    @PostMapping("update")
    public String submitUpdateMember(@Valid BoardFormDto boardDto,
                                     BindingResult bindingResult,
                                     Model model){
        if(bindingResult.hasErrors())
            return "board/view";

        try {
            boardService.savePost(boardDto);
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "board/view";
        }
        return "redirect:/board/list";
    }
    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable(name="id") Long id){
        boardService.deleteBoardById(id);
        return "redirect:/board/list";
    }




}
