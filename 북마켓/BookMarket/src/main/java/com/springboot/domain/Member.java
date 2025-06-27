package com.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name="member")
@Data
//@Builder
public class Member {
    @Id
    @Column(name="num")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Num;

    @Column(unique = true)  // memberId 중복 허용 금지
    private String memberId;

    private String password;
    private String name;
    private String phone;
    private String email;
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member=new Member();
        member.setMemberId(memberFormDto.getMemberId());
        member.setName(memberFormDto.getName());
        member.setPhone(memberFormDto.getPhone());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password=passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return member;
    }

    // Builder 툴로 만들 수 있음
//    public static Member createMember(MemberFormDto dto, PasswordEncoder passwordEncoder) {
//        return Member.builder()
//                .memberId(dto.getMemberId())
//                .password(passwordEncoder.encode(dto.getPassword()))
//                .name(dto.getName())
//                .phone(dto.getPhone())
//                .email(dto.getEmail())
//                .address(dto.getAddress())
//                .role(Role.USER)
//                .build();
//    }

    public void updateFromDto(MemberFormDto dto, PasswordEncoder passwordEncoder) {
        this.name=dto.getName();
        this.phone=dto.getPhone();
        this.email=dto.getEmail();
        this.address=dto.getAddress();
        if(dto.getPassword()!=null && !dto.getPassword().isBlank()){
            this.password=passwordEncoder.encode(dto.getPassword());
        }
    }


}
