package com.example.demo.dto;

import lombok.Data;

@Data
public class MemberDto {
    private Long id;
    private String userId;
    private String userPw;
    private String nickname;
    private String gender;
    private String birth;
    private int residenceType;
}
