package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Users;
import com.example.demo.oauth2.CustomOAuth2User;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/user/logout")
    public String logout(){
        return "redirect:/logout";
    }

    @GetMapping("/")
    public String welcome(Authentication authentication, Model model) {
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            model.addAttribute("loginUser", userDetails.getUser());
        } else if (authentication != null && authentication.getPrincipal() instanceof CustomOAuth2User oauth2User) {
            model.addAttribute("loginUser", oauth2User.getUser());
        }
        return "welcome";
    }

}