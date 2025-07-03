package com.example.demo.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomOAuth2User  implements OAuth2User {
    private final Users user;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(Users user, Map<String, Object> attributes){
        this.user=user;
        this.attributes=attributes;
    }

    @Override
    public String getName() {
        return user.getEmail();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(()-> "ROLE_USER");
    }
}
