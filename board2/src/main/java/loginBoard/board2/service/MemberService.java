package loginBoard.board2.service;

import loginBoard.board2.dto.MemberDTO;
import loginBoard.board2.entity.Member;
import loginBoard.board2.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public Member register(MemberDTO dto) {
        Member member = new Member();
        member.setUsername(dto.getUsername());
        member.setPassword((dto.getPassword()));
        return memberRepository.save(member);
    }

    public Member login(MemberDTO dto) {
        return memberRepository.findByUsername(dto.getUsername())
                .filter(m -> m.getPassword().equals(dto.getPassword()))
                .orElse(null);
    }

    public void changePassword(Long id, String newPassword) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        member.setPassword(newPassword);
        memberRepository.save(member);
    }
}
