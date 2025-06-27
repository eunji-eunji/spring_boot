package springboot.test.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import springboot.test.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByMemberId(String memberId);
}
