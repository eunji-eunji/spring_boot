package com.springboot.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
public class Example02Controller {
//    @GetMapping("/exam02")
//    public String requestMethod(Model model) {
//
//        return "redirect:/member/user";
//    }

//    @GetMapping("/member/user")
//    public String requestMethod2(Authentication authentication, Model model) {
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String username = userDetails.getUsername();  // 사용자 이름 가져오기
//        String password  = userDetails.getPassword(); // 사용자 비밀번호 가져오기
//        Boolean isAuthentication  = authentication.isAuthenticated(); // 사용자의 역할 권한 가져옴
//
//        model.addAttribute("data1", "/manager/user");
//        model.addAttribute("data2", username);
//        model.addAttribute("data3", password);
//
//
//        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
//        for (GrantedAuthority item : authorities ) {
//            model.addAttribute("data4", item+ " ");
//        }
//        model.addAttribute("data5", isAuthentication);
//
//        return "viewPage02";
//
//    }
}
