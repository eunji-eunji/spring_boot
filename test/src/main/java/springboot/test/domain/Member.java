package springboot.test.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

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

    //회원이 작성한 글
    @OneToMany(mappedBy = "writer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

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
