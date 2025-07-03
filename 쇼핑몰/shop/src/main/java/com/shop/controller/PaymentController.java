package com.shop.controller;

import com.shop.service.CartService;
import com.shop.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PaymentController {
    private final OrderService orderService;
    private final CartService cartService;

    @GetMapping
    public String paymentRequest(HttpServletRequest request, Model model){

    }
}
