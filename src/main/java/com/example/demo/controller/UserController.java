package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Users;
import com.example.demo.oauth2.CustomOAuth2User;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public String myPage(Authentication authentication, Model model) {
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            model.addAttribute("loginUser", userDetails.getUser());
        } else if (authentication != null && authentication.getPrincipal() instanceof CustomOAuth2User oauth2User) {
            model.addAttribute("loginUser", oauth2User.getUser());
        }
        return "/user/mypage";
    }

    @GetMapping("/edit")
    public String editForm(Authentication authentication, Model model) {
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            model.addAttribute("loginUser", userService.findById(userDetails.getId()));
        } else if (authentication != null && authentication.getPrincipal() instanceof CustomOAuth2User oauth2User) {
            model.addAttribute("loginUser", userService.findById(oauth2User.getUser().getId()));
        }
        return "/user/edit";
    }

    @PostMapping("/edit")
    public String editInfo(@ModelAttribute UserDto dto,
                           Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            userService.edit(dto, userDetails.getId());
        } else if (authentication != null && authentication.getPrincipal() instanceof CustomOAuth2User oauth2User) {
            userService.edit(dto, oauth2User.getUser().getId());
        }
        return "redirect:/user/mypage";
    }

    @PostMapping("/delete")
    public String delete(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            userService.delete(userDetails.getId());
        } else if (authentication != null && authentication.getPrincipal() instanceof CustomOAuth2User oauth2User) {
            userService.delete(oauth2User.getUser().getId());
        }
        return "redirect:/logout";
    }

    @GetMapping("/checkEmail")
    @ResponseBody
    public Map<String, Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return Map.of("exists", exists);
    }

}