package springboot.test.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import springboot.test.domain.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAll();
}
