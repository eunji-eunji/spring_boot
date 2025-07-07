package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Users;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(UserDto dto) {
        Users user = Users.createUser(dto, passwordEncoder);

        userRepository.save(user);
    }

    public Users findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Users edit(UserDto dto, Long id) {
        Users user = userRepository.findById(id).orElseThrow();

        user.updateUser(dto, passwordEncoder);  // 핵심 변경
        return user;
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
