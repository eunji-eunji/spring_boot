package RestapiBoard.board1.repository;

import RestapiBoard.board1.dto.BoardDTO;
import RestapiBoard.board1.entity.Board;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BoardRepository extends CrudRepository<Board, Long> {
    @Override
    List<Board> findAll();
}
