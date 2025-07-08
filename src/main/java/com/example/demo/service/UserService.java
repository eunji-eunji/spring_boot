package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Users;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public Optional<Users> findByNameAndPhone(String name, String phone) {
        return userRepository.findByNameAndPhone(name, phone);
    }

    public Optional<Users> findByEmailAndPhone(String email, String phone) {
        return userRepository.findByEmailAndPhone(email, phone);
    }

    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void updatePassword(String email, String rawPassword) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String encodedPw = passwordEncoder.encode(rawPassword);
        user.setUserPw(encodedPw);

        userRepository.save(user);
    }

}
