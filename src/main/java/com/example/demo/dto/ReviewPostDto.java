package com.example.demo.dto;

import com.example.demo.entity.ReviewPost;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewPostDto {

    private Long id;

    private String title;
    private String content;

    private List<MultipartFile> files;     // 업로드용 파일 리스트

    private String fileNames;              // 저장된 파일 이름 (쉼표 구분)
    private String filePaths;              // 저장된 파일 경로 (쉼표 구분)
    private List<String> filePathList;     // 미리보기용 리스트 변환용

    private int rating;

    private String nickname;               // 작성자 닉네임
    private String email;                  // 작성자 이메일

    private Long productId;                // 상품 ID
    private String productName;            // 상품 이름 (뷰에 노출용)

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private boolean likedByCurrentUser;    // 좋아요 여부
    private int liked;                     // 좋아요 수

    // Entity → DTO 변환
    public static ReviewPostDto fromEntity(ReviewPost post) {
        List<String> filePathList = null;
        if (post.getFilePaths() != null && !post.getFilePaths().isEmpty()) {
            filePathList = Arrays.stream(post.getFilePaths().split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
        }

        return ReviewPostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .fileNames(post.getFileNames())
                .filePaths(post.getFilePaths())
                .filePathList(filePathList)
                .rating(post.getRating())
                .nickname(post.getUser().getNickname())
                .email(post.getUser().getEmail())
                .productId(post.getProduct().getId())
                .productName(post.getProduct().getName())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
