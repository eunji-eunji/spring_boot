package com.springboot.controller;

import com.springboot.domain.Member;
import com.springboot.domain.Product;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class Ex01Controller {
//
//    @GetMapping("/ex01")
//    public String ShowFrom(Model model){
//        model.addAttribute("product", new Product());
//        return "view01";
//    }
//    @PostMapping("/ex01")
//    public String submit(@Valid @ModelAttribute Product product, BindingResult bindingResult, Model model){
//
//        if(bindingResult.hasErrors())
//            return "view01";
//
//        model.addAttribute("product", product);
//        return "view01_result";
//    }
//
//    @GetMapping("/exam03")
//    public String ShowFrom3(Model model){
//        model.addAttribute("member", new Member());
//        return "view03";
//    }
//    @PostMapping("/exam03")
//    public String submit3(@Valid @ModelAttribute Member member, BindingResult bindingResult, Model model){
//
//        if(bindingResult.hasErrors())
//            return "view03";
//
//        model.addAttribute("member", member);
//        return "view03_result";
//    }


}
