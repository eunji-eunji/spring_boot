package loginBoard.board2.repository;

import loginBoard.board2.entity.Board;
import loginBoard.board2.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByIdDesc();

    List<Board> findByWriter(Member writer);
    
    // 거꾸로 정렬해서 가져옴
}
