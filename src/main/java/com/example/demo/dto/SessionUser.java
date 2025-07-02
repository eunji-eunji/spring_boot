package com.example.demo.dto;

import com.example.demo.entity.Users;

import java.io.Serializable;

public class SessionUser implements Serializable {
    private String name;
    private String email;

    public SessionUser(Users user) {
        this.name = name;
        this.email = email;
    }
}
