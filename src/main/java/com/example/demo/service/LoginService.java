package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Users;
import com.example.demo.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final LoginRepository loginRepository;

    public Users login(UserDto dto) {
        return loginRepository.findByEmail(dto.getEmail())
                .filter(m -> m.getUserPw().equals(dto.getUserPw()))
                .orElse(null);
    }
}
