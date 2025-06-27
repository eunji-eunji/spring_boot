package member.member1.controller;


import member.member1.dto.MemberDto;
import member.member1.entity.Member;
import member.member1.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    //회원가입폼 보여주기
    @GetMapping("/new")
    public String createForm(Model model){
        //비어있는 MemberDto 객체를 파일로 전달
        model.addAttribute("memberDto",new MemberDto());
        return "members/create";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute MemberDto dto){
        memberService.create(dto);
        return "redirect:/members";
    }

    // read - 회원 목록 보기
    @GetMapping
    public String list(Model model){
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "members/list";
    }

    //update - 수정
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model){
        Member member = memberService.findById(id);
        if(member == null) return "redirect:/members";
        //entity를 dto 로 바꿔줌
        MemberDto dto=new MemberDto(member.getId(), member.getUsername(), member.getEmail(), member.getPassword());
        model.addAttribute("memberDto", dto);
        return "members/edit";
    }

    //update - 수정 처리
    @PostMapping("/{id}/edit")  // 저장하려면 PostMapping, 불러오는 건 GetMapping
    public String edit(@PathVariable Long id, @ModelAttribute MemberDto dto){
        memberService.update(id, dto);
        return "redirect:/members";
    }

    //delete - 삭제
    @PostMapping("/{id}/delete")  // 저장하려면 PostMapping, 불러오는 건 GetMapping
    public String delete(@PathVariable Long id){
        memberService.delete(id);
        return "redirect:/members";
    }
}
