package com.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Example01Controller {
//    @GetMapping("/exam01")
//    public String requestMethod33(Model model) {
//        return "viewPage01";
//    }
//
//    @GetMapping("/home/main")
//    public String requestMethod5(Model model) {
//        model.addAttribute("data", "homePage.html");
//        return "homePage";
//    }
//    @GetMapping("/member/main")
//    public String requestMethod4(Model model){
//        model.addAttribute("data", "memberPage.html");
//        return "memberPage";
//    }
//
//    @GetMapping("/manager/main")
//    public String requestMethod3(Model model) {
//        model.addAttribute("data", "managerPage.html");
//        return "managerPage";
//    }

//    @GetMapping("/admin/main")
//    public String requestMethod2(Model model) {
//        model.addAttribute("data", "adminPage.html");
//
//
//    /*     Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//         UserDetails userDetails = (UserDetails)principal;
//
//
//
//         String username = userDetails.getUsername();
//         String password = userDetails.getPassword();
//         String role = userDetails.getAuthorities().toString();
//
//         model.addAttribute("data2", username);
//         model.addAttribute("data3", password);
//         model.addAttribute("data4", role);
//         */
//        return "adminPage";
//    }
}
