package com.example.demo.service;

import com.example.demo.entity.Users;
import com.example.demo.oauth2.OAuthAttributes;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final HttpSession session;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        OAuth2User oAuth2User = super.loadUser(request);

        //어떤 플랫폼인지
        String registrationId = request.getClientRegistration().getRegistrationId(); // google, kakao, naver
        String userNameAttribute = request.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 플랫폼별 사용자 정보 파싱
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttribute, oAuth2User.getAttributes());
        //이메일로 기존 회원 조회
        Users user = userRepository.findByEmail(attributes.getEmail())
                .orElseGet(() -> saveUser(attributes)); // 없으면 저장
        //세션에 로그인 사용자 정보 저장
        session.setAttribute("loginUser", user);
        //반환값은 사용하지 않음, 단순히 인증 완료 트리거용
        return oAuth2User;
    }

    private Users saveUser(OAuthAttributes attr) {
        Users user = Users.builder()
                .email(attr.getEmail())
                .nickname(attr.getName())
                .userPw("")
                .phone("")
                .gender("")
                .birth(LocalDate.now())
                //생일 정보 수정 권유 팝업 띄우기
                .p_code("")
                .loadAddr("")
                .lotAddr("")
                .detailAddr("")
                .extraAddr("")
                .residenceType(0)
                .social(attr.getProvider())
                .build();
        return userRepository.save(user);
    }
}
