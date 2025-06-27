package com.example.membercrud.controller;

import com.example.membercrud.dto.MemberDto;
import com.example.membercrud.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller // 이 클래스가 웹 요청을 처리하는 컨트롤러라는 의미
@RequestMapping("/members") // 모든 메서드는 URL이 "/members"로 시작됨
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/new")
    public String newForm(Model model){
        model.addAttribute("member", new MemberDto());
        return "members/member-form";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute MemberDto dto){
        memberService.save(dto);
        return "redirect:/members";
    }

    @GetMapping
    public String list(Model model){
        //List<MemberDto> memberDtoList = memberService.findAll();
        model.addAttribute("members", memberService.findAll());
        return "members/members";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model){
        model.addAttribute("member", memberService.findById(id));
        return "/members/edit-member";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute MemberDto dto){
        dto.setId(id);
        memberService.save(dto);
        return "redirect:/members";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id){
        memberService.deleteById(id);
        return "redirect:/members";
    }
}
