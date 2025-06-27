package com.springboot.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Example05Controller {
    @GetMapping("/exam05")
    public String requestMethod(Model model) {
        return "viewPage05";
    }

    /*   @GetMapping("/admin")
       public String requestMethod2(@AuthenticationPrincipal User user, Model model) {
          model.addAttribute("data", user.getUsername());
             return  "viewPage03";
       }

       */
    @GetMapping("/admin")
    public String requestMethod2(Authentication user, Model model) {
        model.addAttribute("data", user.getName());
        return  "viewPage05_result";
    }
   /*
   @GetMapping("/admin")
   public String requestMethod3(Principal user, Model model) {

      model.addAttribute("data", user.getName());
         return  "viewPage04_result";
   }

*/


    @GetMapping("/logout")
    public String logout(Model model) {
        return "viewPage05";
    }
}
