package board3.board.service;


import board3.board.dto.MemberDTO;
import board3.board.entity.Member;
import board3.board.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;


    public Member register(MemberDTO dto) {
        Member member = new Member();
        member.setUsername(dto.getUsername());
        member.setPassword(dto.getPassword());
        return memberRepository.save(member);
    }

    public Member login(MemberDTO dto) {
        return memberRepository.findByUsername(dto.getUsername())
                .filter(m -> m.getPassword().equals(dto.getPassword()))
                .orElse(null);
    }

    public void changePassword(Long id, String newPassword) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없음"));
        member.setPassword(newPassword);
        memberRepository.save(member);

    }
}