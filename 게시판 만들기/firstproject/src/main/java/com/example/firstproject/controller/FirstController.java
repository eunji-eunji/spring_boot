package com.example.firstproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FirstController {
    @GetMapping("/hi")  // 요청 url
    public String niceToMeetYou(Model model) {
        model.addAttribute("username", "정인");
        // username이라는 변수에 "정인"이라는 값을 넣음
        return "greetings"; // 보여줄 페이지 return
    }
    @GetMapping("/bye")  // 요청 url
    public String seeYouNext(Model model) {
        model.addAttribute("nickname", "홍길동");
        // username이라는 변수에 "정인"이라는 값을 넣음
        return "goodbye"; // 보여줄 페이지 return
    }
}
