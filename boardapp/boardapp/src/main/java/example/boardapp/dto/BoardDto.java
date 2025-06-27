package example.boardapp.dto;

import example.boardapp.entity.Board;

public class BoardDto {
    private Long id;
    private String title;
    private String content;
    private String writer;

    public BoardDto() {
    }
    // Entity -> DTO 변환 생성자
    public BoardDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getWriter();
    }
    // DTO -> Entity 변환 메서드
    public Board toEntity() {
        return new Board(id, title, content, writer);
    }

    public BoardDto(String title, String content, String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }


}
