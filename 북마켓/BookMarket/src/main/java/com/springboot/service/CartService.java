package com.springboot.service;

import com.springboot.domain.Cart;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    Cart create(Cart cart);
    Cart read(String cartId);
    void update(String cartId, Cart cart);
    void delete(String cartId);

}
