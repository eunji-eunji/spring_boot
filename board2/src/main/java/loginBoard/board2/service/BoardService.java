package loginBoard.board2.service;

import loginBoard.board2.dto.BoardDTO;
import loginBoard.board2.entity.Board;
import loginBoard.board2.entity.Member;
import loginBoard.board2.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    public Board create(BoardDTO dto, Member writer) {
        Board board = new Board();
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        board.setWriter(writer);
        return boardRepository.save(board);
    }

    public List<Board> list() {
        return boardRepository.findAllByOrderByIdDesc();
    }

    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("해당 게시글이 없습니다."));
    }

    public void update(Long id, BoardDTO dto) {
        Board board = findById(id);
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        boardRepository.save(board);
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public List<Board> findByWriter(Member writer) {
        return boardRepository.findByWriter(writer);
    }
}
