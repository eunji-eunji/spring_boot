package com.example.demo.service;

import com.example.demo.dto.MemberDto;
import com.example.demo.entity.Member;
import com.example.demo.repository.LoginRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final LoginRepository loginRepository;

    public Member login(MemberDto dto) {
        return loginRepository.findByUserId(dto.getUserId())
                .filter(m -> m.getUserPw().equals(dto.getUserPw()))
                .orElse(null);
    }
}
