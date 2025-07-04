package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Users;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor

public class LoginController {
    private final LoginService loginService;

    @GetMapping("/user/login")
    public String loginForm() {
        return "user/login";
    }

    /*@PostMapping("/user/login")
    public String login(UserDto dto, HttpSession session){
        Users user = loginService.login(dto);
        if(user != null){
            session.setAttribute("loginUser", user);
            return "redirect:/";
        }else{
            return "redirect:/user/login?error=true";
        }
    }*/

    @GetMapping("/user/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/")
    public String welcome(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            model.addAttribute("loginUser", userDetails.getUser());
        }
        return "welcome";
    }

}