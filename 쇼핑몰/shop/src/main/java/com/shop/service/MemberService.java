package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember=memberRepository.findByEmail(member.getEmail());
        if(findMember != null)
            throw new IllegalArgumentException("이미 가입된 회원입니다.");

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        // 사용자 정보를 조회, 스프링 시큐리티가 이해할 수 있는 UserDetails로 변환
        return User.builder()
                .username(member.getEmail())    //로그인으로 사용할 필드
                .password(member.getPassword()) //DB에 암호화된 비밀번호
                .roles(member.getRole().toString()) //Role_ 접두사가 자동 등록
                .build();
    }
}
