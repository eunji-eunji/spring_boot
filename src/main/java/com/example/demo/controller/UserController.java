package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Users;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String singupForm() {
        return "/user/signUp";
    }

    @PostMapping("signup")
    public String signUp(@ModelAttribute UserDto dto){
        userService.signUp(dto);
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model){
        Users loginUser = (Users) session.getAttribute("loginUser");
        model.addAttribute("loginUser", loginUser);
        return "/user/mypage";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("loginUser", userService.findById(id));
        return "/user/edit";
    }

    @PostMapping("/edit/{id}")
    public String editInfo(@PathVariable("id") Long id, @ModelAttribute UserDto dto, HttpSession session){
        Users updateUser = userService.edit(dto, id);
        session.setAttribute("loginUser", updateUser);
        return "redirect:/user/mypage";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, HttpSession session) {
        userService.delete(id);
        session.invalidate();
        return "redirect:/";
    }

}
