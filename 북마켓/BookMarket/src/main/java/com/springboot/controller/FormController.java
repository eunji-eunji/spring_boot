package com.springboot.controller;

import com.springboot.domain.Member;
import com.springboot.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.Authenticator;

@Controller
public class FormController {
    @Autowired
    private MemberService memberService;
    @RequestMapping("/")
    public String welcome(Model model, Authentication authenticator, HttpServletRequest httpServletRequest){
        if(authenticator == null)
            return "welcome";

        User user=(User)authenticator.getPrincipal();
        String userId=user.getUsername();

        if(userId==null)
            return "redirect:/login";

        // 회원 정보 조회
        Member member=memberService.getMemberById(userId);

        // 세션을 생성하기 전에 기존의 세션 파기
        //httpServletRequest.getSession().invalidate();

        // 세션 새로 생성(필요 시)
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute("userLoginInfo", member);
        return "welcome";
    }
}
