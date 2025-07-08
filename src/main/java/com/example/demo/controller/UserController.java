package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.PasswordResetToken;
import com.example.demo.entity.Users;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.EmailService;
import com.example.demo.service.PasswordResetService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PasswordResetService passwordResetService;
    private final EmailService emailService;

    @GetMapping("/signup")
    public String singupForm() {
        return "/user/signUp";
    }

    @PostMapping("signup")
    public String signUp(@ModelAttribute UserDto dto) {
        userService.signUp(dto);
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String myPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("loginUser", userDetails.getUser()); // Users 엔티티 전달
        return "/user/mypage";
    }

    @GetMapping("/edit")
    public String editForm(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Users user = userService.findById(userDetails.getId());
        model.addAttribute("loginUser", user);
        return "/user/edit";
    }

    @PostMapping("/edit")
    public String editInfo(@ModelAttribute UserDto dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.edit(dto, userDetails.getId());
        return "redirect:/user/mypage";
    }

    @PostMapping("/delete")
    public String delete(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.delete(userDetails.getId());
        return "redirect:/logout";
    }

    @GetMapping("/checkEmail")
    @ResponseBody
    public Map<String, Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return Map.of("exists", exists);
    }

    @GetMapping("/findId")
    public String findIdForm() {
        return "/user/findID";
    }

    @PostMapping("/findId")
    public String findUserId(@RequestParam String name, @RequestParam String phone, Model model) {
        Optional<Users> findUser = userService.findByNameAndPhone(name, phone);
        if (findUser.isPresent()) {
            model.addAttribute("email", findUser.get().getEmail());
        } else {
            model.addAttribute("error", "일치하는 회원 정보가 없습니다.");
        }
        return "user/findID";
    }

    @GetMapping("/findPw")
    public String findPwForm() {
        return "/user/findPW";
    }

    @PostMapping("/findPw")
    public String findUserPw(@RequestParam String email, @RequestParam String phone,Model model) {

        Optional<Users> findUser = userService.findByEmailAndPhone(email, phone);

        if (findUser.isPresent()) {
            // 토큰 생성
            String token = passwordResetService.createToken(email);
            // 이메일 전송
            emailService.sendResetPasswordLink(email, token);
            // 모달에 보일 메시지 전달
            model.addAttribute("resetMailSent", "입력하신 이메일로 비밀번호 재설정 링크를 전송했습니다.");
        } else {
            model.addAttribute("error", "일치하는 회원 정보가 없습니다.");
        }

        return "user/findPW";
    }

    @GetMapping("/resetPw")
    public String showResetPwForm(@RequestParam("token") String token, Model model) {
        PasswordResetToken validToken = passwordResetService.validateToken(token);

        if (validToken == null) {
            model.addAttribute("error", "유효하지 않거나 만료된 토큰입니다.");
            return "user/resetPwError"; // 에러 페이지 또는 모달 처리
        }

        model.addAttribute("token", token);
        return "user/resetPw";
    }

    @PostMapping("/resetPw")
    public String resetPassword(@RequestParam String token, @RequestParam String newPw, @RequestParam String confirmPw, Model model) {

        PasswordResetToken resetToken = passwordResetService.validateToken(token);

        if (resetToken == null) {
            model.addAttribute("error", "유효하지 않거나 만료된 토큰입니다.");
            return "user/resetPwError";
        }

        if (!newPw.equals(confirmPw)) {
            model.addAttribute("token", token);
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "user/resetPw";
        }

        userService.updatePassword(resetToken.getEmail(), newPw);

        passwordResetService.markTokenUsed(resetToken);

        return "redirect:/user/login?resetSuccess";
    }

}