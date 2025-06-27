package com.example.membercrud.mapper;

import com.example.membercrud.dto.MemberDto;
import com.example.membercrud.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public Member toEntity(MemberDto dto){
        return Member.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .build();
    }

    public MemberDto toDto(Member member){
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .password(member.getPassword())
                .email(member.getEmail())
                .build();
    }
}
