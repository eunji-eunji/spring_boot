package com.example.oauth2.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString

public class UserPwRequestDto {

    private String userName;

    private String userEmail;

    private String userId;
}
