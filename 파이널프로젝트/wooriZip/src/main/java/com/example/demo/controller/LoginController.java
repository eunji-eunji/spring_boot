package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Users;
import com.example.demo.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    @GetMapping("/")
    public String welcome(@AuthenticationPrincipal UserDetails userDetails, Model model){
        model.addAttribute("loginUser", userDetails);
        return "welcome";
    }
}
