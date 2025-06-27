package com.example.board01.dto;

import com.example.board01.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BoardDTO {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdDate;

    public BoardDTO(Long id, String title, String content, String writer, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.createdDate = createdDate;
    }

    //dto - > entity 변환
//    public Board toEntity(){
//        return new Board(id, title, content, writer, createdDate);
//    }
    //dto - > entity 변환 builder 방식
    public  Board toEntity(){
        return Board.builder()
                .id(id)
                .title(title)
                .content(content)
                .writer(writer)
                .createdDate(createdDate)
                .build();
    }

    public BoardDTO() {
    }
}
