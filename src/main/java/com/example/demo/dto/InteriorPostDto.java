package com.example.demo.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class    InteriorPostDto {
    private Long postId;
    private String title;
    private String content;

    private List<MultipartFile> files;
    private String fileNames;
    private String filePaths;
    private List<String> filePathList;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String email;
    private String nickname;

    private int views;
    private int liked;
    private boolean likedByCurrentUser;

}