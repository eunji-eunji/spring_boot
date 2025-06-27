package com.example.oauth2.service;


import com.example.oauth2.dto.*;
import com.example.oauth2.entity.Users;
import com.example.oauth2.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final SendService sendService;
    private final PasswordEncoder passwordEncoder;

//    Spring Security가 로그인 시 호출하는 메서드 반드시 오버라이드 해야함.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다 : " + username));
        return User.builder() // Security에서 사용하는 User임 세션을 관리함 위의 Users user랑은 다르다.
                .username(user.getUserId()) //로그인으로 사용할 필드
                .password(user.getUserPw()) //DB에 암호화된 비밀번호
                .roles(user.getRole().toString()) //Role 접두사가 자동으로 등록
                .build();
    }

    @Transactional
    public void save(AddUserRequest dto){
        userRepository.save(Users.builder()
                .email(dto.getEmail())
                .userPw(passwordEncoder.encode(dto.getPassword()))
                .build());
    }

    @Transactional
    public void JoinUser(UserDto userDto){
        Optional<Users> existingUser = userRepository.findById(userDto.getUserId());
        if (existingUser.isPresent()){
            throw new IllegalStateException("User Id already exists");
        }
        Users user = Users.createMember(userDto, passwordEncoder);
        userRepository.save(user);
    }

    public boolean checkUserIdExists(String userId) {
        return userRepository.findById(userId).isPresent();
    }

    public Users getUserByEmailAndPhoneNumber(String email, String phone) {
        return userRepository.findByEmailAndPhone(email, phone).orElseThrow(EntityNotFoundException::new);
    }
//  비밀번호 찾기
    public void userCheck(UserPwRequestDto requestDto) {
        Users user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("사용자의 이메일이 설정되지 않았습니다.");
        }
        // requestDto에 들어있는 userId를 조회하고
        // 그 회원의 이메일 requestDto 셋팅 후
        requestDto.setUserEmail(user.getEmail());
        sendEmail(requestDto);
        // sendEmail을 메서드 호출해서 비밀번호 재설정 메일을 전송
    }

    public void sendEmail(UserPwRequestDto requestDto) {
        if (requestDto.getUserEmail() == null || requestDto.getUserEmail().isEmpty()) {
            throw new IllegalArgumentException("Recipient email address cannot be null or empty.");
        }
        // 임시 비밀번호 메일 생성하고 전송

        MailDto dto = sendService.createMailAndChargePassword(requestDto);
        sendService.mailSend(dto);
    }
    // User 정보수정
    public UserEditDto getUserById(String userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserEditDto(user);
    }

    // 수정한 정보를 저장
    @Transactional
    public boolean updateUser(UserEditDto userEditDto) {
        try {
            log.info("수정한값 : {} ", userEditDto);
            Users user = userRepository.findById(userEditDto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (userEditDto.getUserPw() != null && !userEditDto.getUserPw().isEmpty()) {
                user.setUserPw(passwordEncoder.encode(userEditDto.getUserPw()));
            }
            user.setPCode(userEditDto.getPCode());
            user.setLoadAddr(userEditDto.getLoadAddr());
            user.setLotAddr(userEditDto.getLotAddr());
            user.setDetailAddr(userEditDto.getDetailAddr());
            user.setExtraAddr(userEditDto.getExtraAddr());
            user.setPhone(userEditDto.getPhone());
            user.setEmail(userEditDto.getEmail());
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            log.error("Error updating user: ", e);
            return false;
        }
    }


    // 소셜 로그인 시도시 회원정보가 비어있는지 없는지 체크
    public boolean isProfileComplete(String userid) {
        Users user = userRepository.findById(userid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 검사 항목들
        String pCode = user.getPCode();
        String loadAddr = user.getLoadAddr();
        String lotAddr = user.getLotAddr();
        String detailAddr = user.getDetailAddr();

        List<String> fieldCheckList = Arrays.asList(pCode, loadAddr, lotAddr, detailAddr);

        return fieldCheckList.stream().allMatch(this::isFieldValid);
    }
    // 각 필드 검사
    private boolean isFieldValid(String field) {
        return field != null && !field.isEmpty();
    }



    // 탈퇴
    public void deleteUserById(String userId) {
        Users users = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("찾는 유저가 없습니다"));

        userRepository.delete(users);
    }


// Users 직접 만든 엔티티
// DB 저장용, 회원정보관리
// DB에서 조회한 후 User로 변환

// User(Security에서 제공)
// 인증용 Spring Security에서 로그인처리 -> 인증 시스템 전달용
// principal.getName(); 해서 시큐리티 로그인한 사용자 불러올 수 있다.

//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    Object principal = auth.getPrincipal();
//
//    if (principal instanceof UserDetails userDetails) {
//        System.out.println("일반 로그인 ID: " + userDetails.getUsername());
//        System.out.println("getName: " + auth.getName()); // 동일함
//    } else if (principal instanceof OAuth2User oauthUser) {
//        System.out.println("소셜 로그인 ID: " + oauthUser.getAttribute("userId"));
//        System.out.println("getName: " + oauthUser.getName()); // "userId" 필드의 값
//    }


}
