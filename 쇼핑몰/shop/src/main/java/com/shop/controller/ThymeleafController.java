package com.shop.controller;

import com.shop.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafController {
    @GetMapping("/ex01")
    public String thymeleafEx01(Model model){
        model.addAttribute("data", "타임리프 예제");
        return "thymeleaf/thymeleafEx01";
    }

    @GetMapping("/ex02")
    public String thymeleafEx02(Model model){
        ItemDto itemDto=new ItemDto();
        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemNm("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());
        model.addAttribute("itemDto", itemDto);
        return "thymeleaf/thymeleafEx02";
    }

    @GetMapping("/ex03")
    public String thymeleafEx03(Model model){
        List<ItemDto> itemDtoList=new ArrayList<>();
        for(int i=0;i<10;i++){
            ItemDto itemDto=new ItemDto();
            itemDto.setItemDetail("상품 상세 설명"+i);
            itemDto.setItemNm("테스트 상품"+i);
            itemDto.setPrice(1000*i);
            itemDto.setRegTime(LocalDateTime.now());
            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleaf/thymeleafEx03";
    }

    @GetMapping("/ex04")
    public String thymeleafEx04(Model model){
        List<ItemDto> itemDtoList=new ArrayList<>();
        for(int i=0;i<10;i++){
            ItemDto itemDto=new ItemDto();
            itemDto.setItemDetail("상품 상세 설명"+i);
            itemDto.setItemNm("테스트 상품"+i);
            itemDto.setPrice(1000*i);
            itemDto.setRegTime(LocalDateTime.now());
            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleaf/thymeleafEx04";
    }

    @GetMapping("/ex05")
    public String thymeleafEx05(){
        return "thymeleaf/thymeleafEx05";
    }

    @GetMapping("/ex06")
    public String thymeleafEx06(String param1, String param2, Model model){
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleaf/thymeleafEx06";
    }

    @GetMapping("/ex07")
    public String thymeleafEx07(){
        return "thymeleaf/thymeleafEx07";
    }
}
