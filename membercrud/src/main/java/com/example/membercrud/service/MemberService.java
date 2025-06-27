package com.example.membercrud.service;

import com.example.membercrud.dto.MemberDto;
import com.example.membercrud.entity.Member;
import com.example.membercrud.mapper.MemberMapper;
import com.example.membercrud.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberMapper memberMapper;

    public void save(MemberDto dto) {
        memberRepository.save(memberMapper.toEntity(dto));
        // dto -> entity 변환 후 저장
    }

    public List<MemberDto> findAll() {
//        List<Member> memberList = memberRepository.findAll();
//        List<MemberDto> memberDtoList = new ArrayList<>();
//        for(Member member : memberList) {
//            memberDtoList.add(memberMapper.toDto(member));
//        }

        return memberRepository.findAll().stream()
                .map(memberMapper::toDto).toList();
    }

    public MemberDto findById(Long id) {
        return memberMapper.toDto(
            memberRepository.findById(id).orElseThrow()
        );
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }
}
