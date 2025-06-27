package board3.board.controller;

import board3.board.dto.MemberDTO;
import board3.board.entity.Member;
import board3.board.service.MemberService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/register")
    public String registerForm(){
        return "register";
    }
    @PostMapping("/register")
    public String register(MemberDTO dto){
        memberService.register(dto);
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
    @PostMapping("/login")
    public  String login(MemberDTO dto, HttpSession session){
        Member member = memberService.login(dto);
        if(member != null){
            session.setAttribute("loginUser", member);
            return "redirect:/boards";
        }else{
            return  "redirect:/login?error=true";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }


}
