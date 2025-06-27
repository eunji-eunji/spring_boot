package example.boardapp.service;

import example.boardapp.dto.BoardDto;
import example.boardapp.entity.Board;
import example.boardapp.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    public void create(BoardDto dto) {
        Board board = new Board(dto);
        boardRepository.save(board);
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }


    public Board findById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    public void update(Long id, BoardDto dto) {
        Optional<Board> optionalBoard =boardRepository.findById(id);
        if(optionalBoard.isPresent()){
            Board board = optionalBoard.get();
            board = dto.toEntity();
            boardRepository.save(board);
        }
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }
}
