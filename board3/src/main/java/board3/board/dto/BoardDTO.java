package board3.board.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class BoardDTO {
    private String title;
    private String content;
    private MultipartFile image;
}
