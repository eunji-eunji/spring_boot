package RestapiBoard.board1.service;

import RestapiBoard.board1.dto.BoardDTO;
import RestapiBoard.board1.entity.Board;
import RestapiBoard.board1.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public Long save(BoardDTO dto) {
        Board board = Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .createdAt(LocalDateTime.now())
                .build();
        return boardRepository.save(board).getId();
    }

    public List<BoardDTO> findAll() {
        return boardRepository.findAll().stream()
                // map(반복)을 돌며 board를 BoardDTO로 변환
                .map(board -> BoardDTO.builder()
                        .id(board.getId())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .writer(board.getWriter())
                        // DTO를 String 타입으로 선언했으므로 format 적용가능
                        .createdAt(board.getCreatedAt().format(FORMATTER))
                        .build())
                .collect(Collectors.toList());
                // stream을 list로 변환
    }

    public BoardDTO findById(Long id) {
        Board board = boardRepository.findById(id).orElseThrow();
        return BoardDTO.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .createdAt(board.getCreatedAt().format(FORMATTER))
                .build();
    }

    public void update(Long id, BoardDTO dto) {
        Board board = boardRepository.findById(id).orElseThrow();
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        board.setWriter(dto.getWriter());
        boardRepository.save(board);
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }
}
