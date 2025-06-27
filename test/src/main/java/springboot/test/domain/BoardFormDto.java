package springboot.test.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class BoardFormDto {
    private Long id;
    private String writerid;
    private String writer;
    private String title;
    private String content;
    private MultipartFile imagePath;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
