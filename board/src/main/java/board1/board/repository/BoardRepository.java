package board1.board.repository;

import board1.board.entity.Board;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends CrudRepository<Board, Long> {
    /*CurdRepository 에 save가 구현되어 있기 때문에 따로 적지 않아도 사용할 수 있음*/
    @Override
    List<Board> findAll();
}
