package springboot.test.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import springboot.test.domain.Board;
import springboot.test.domain.BoardFormDto;
import springboot.test.domain.Member;
import springboot.test.repository.BoardRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Value("${upload.dir}")
    private String uploadDir;


    public List<Board> list() {
        return boardRepository.findAll();
    }

    public void create(BoardFormDto dto, String writerId) throws IOException {
        Board board = new Board();
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        board.setWriter(dto.getWriter());
        board.setWriterid(writerId); // String writer 저장

        // 이미지 저장 로직 그대로 유지
        if (!dto.getImagePath().isEmpty()) {
            String filename = UUID.randomUUID() + "_" + dto.getImagePath().getOriginalFilename();
            Path path = Paths.get(uploadDir, filename);
            Files.createDirectories(path.getParent());
            Files.copy(dto.getImagePath().getInputStream(), path);
            board.setImagePath("/uploads/" + filename);
        }

        boardRepository.save(board);
    }

    public Board findById(Long id) {
        return boardRepository.findById(id).orElseThrow();
    }

    public void update(Long id, BoardFormDto dto) throws IOException {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        // 이미지가 새로 업로드된 경우
        if (!dto.getImagePath().isEmpty()) {
            String filename = UUID.randomUUID() + "_" + dto.getImagePath().getOriginalFilename();
            Path path = Paths.get(uploadDir, filename);
            Files.createDirectories(path.getParent());
            Files.copy(dto.getImagePath().getInputStream(), path);
            board.setImagePath("/uploads/" + filename);
        }
        boardRepository.save(board);
    }


    // 삭제하기
    public void deleteBoardById(Long id){
        boardRepository.deleteById(id);
    }
}
