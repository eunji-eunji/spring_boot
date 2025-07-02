package com.example.demo.controller;

import com.example.demo.dto.InteriorPostDto;
import com.example.demo.dto.PostCommentDto;
import com.example.demo.entity.Users;
import com.example.demo.repository.LoginRepository;
import com.example.demo.service.InteriorPostService;
import com.example.demo.service.PostCommentService;
import com.example.demo.service.PostLikeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/interior")
public class InteriorPostController {

    private final InteriorPostService service;
    private final PostCommentService commentService;
    private final PostLikeService postLikeService;

    /** 글 목록 + 페이지네이션 추가 */
    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int page,
                       Model model,
                       HttpSession session) {

        int pageSize = 10; // 한 페이지에 보여줄 게시글 수
        Page<InteriorPostDto> postPage = service.findPagedPosts(page, pageSize);

        model.addAttribute("postPage", postPage); // 페이지 객체 전달
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());

        Users loginUser = (Users) session.getAttribute("loginUser");
        model.addAttribute("loginUser", loginUser);

        return "interior/list";
    }


    /** 글 작성 폼 */
    @GetMapping("/write")
    public String writeForm(Model model, HttpSession session) {
        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/user/login";

        model.addAttribute("dto", new InteriorPostDto());
        model.addAttribute("loginUser", loginUser);
        return "interior/write";
    }

    /** 글 작성 처리 + 파일 업로드 포함 */
    @PostMapping("/write")
    @ResponseBody
    public String writePost(@ModelAttribute InteriorPostDto dto,
                            @RequestParam(value = "files", required = false) MultipartFile[] files,
                            HttpSession session) {

        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) return "unauthorized";

        dto.setEmail(loginUser.getEmail());
        dto.setNickname(loginUser.getNickname());

        // 이미지가 없어도 등록 가능하게 처리
        if (files != null && files.length > 0 && !files[0].isEmpty()) {
            handleMultipleFiles(dto, files);
        }

        service.save(dto);

        return "success";
    }




    /** 상세 보기 + 조회수 증가 + 댓글 조회 */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         Model model,
                         HttpSession session) {

        Users loginUser = (Users) session.getAttribute("loginUser");

        service.increaseViews(id);
        InteriorPostDto dto = service.findById(id, loginUser);  // ✅ 변경됨

        model.addAttribute("dto", dto);
        model.addAttribute("commentList", commentService.findByPostId(id));
        model.addAttribute("loginUser", loginUser);

        return "interior/detail";
    }


    /** 수정 폼 */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id,
                           Model model,
                           HttpSession session) {

        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/user/login";

        model.addAttribute("dto", service.findById(id, loginUser));  // 수정된 메서드 호출
        model.addAttribute("loginUser", loginUser);

        return "interior/edit";
    }


    /** 수정 */
    @PostMapping("/edit")
    @ResponseBody
    public String edit(@ModelAttribute InteriorPostDto dto,
                       @RequestParam(value = "files", required = false) MultipartFile[] files,
                       @RequestParam(value = "deleteIndexes", required = false) List<Integer> deleteIndexes,
                       HttpSession session) {

        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) return "unauthorized";

        dto.setEmail(loginUser.getEmail());
        dto.setNickname(loginUser.getNickname());

        // 기존 이미지 경로 목록 가져오기
        List<String> existingPaths = new ArrayList<>();
        if (dto.getFilePaths() != null && !dto.getFilePaths().isEmpty()) {
            existingPaths = new ArrayList<>(List.of(dto.getFilePaths().split(",")));
        }

        // 삭제 대상 인덱스 제거
        if (deleteIndexes != null && !deleteIndexes.isEmpty()) {
            // 역순으로 정렬해서 index 밀림 방지
            deleteIndexes.sort(Comparator.reverseOrder());
            for (int index : deleteIndexes) {
                if (index >= 0 && index < existingPaths.size()) {
                    String path = existingPaths.get(index);
                    File file = new File(System.getProperty("user.dir") + path);
                    if (file.exists()) file.delete();
                    existingPaths.remove(index);
                }
            }
        }

        // 새 이미지 추가
        List<String> addedPaths = new ArrayList<>();
        List<String> addedNames = new ArrayList<>();
        if (files != null) {
            List<String>[] result = handleAndReturnFiles(files);
            addedPaths = result[0];
            addedNames = result[1];
        }

        // 최종 경로 저장 (기존 - 삭제 + 추가)
        existingPaths.addAll(addedPaths);
        dto.setFilePaths(String.join(",", existingPaths));
        dto.setFileNames(String.join(",", addedNames));

        service.save(dto);
        return "success";
    }



    /** 삭제 */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         HttpSession session) {

        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/user/login";

        service.delete(id);
        return "redirect:/interior";
    }

    /** 댓글 등록 */
    @PostMapping("/{postId}/comment")
    public String addComment(@PathVariable Long postId,
                             @RequestParam String content,
                             HttpSession session) {

        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/user/login";

        PostCommentDto comment = PostCommentDto.builder()
                .postId(postId)
                .email(loginUser.getEmail())
                .nickname(loginUser.getNickname())
                .content(content)
                .build();

        commentService.save(comment);
        return "redirect:/interior/" + postId;
    }

    /** 댓글 삭제 */
    @PostMapping("/{postId}/comment/delete/{commentId}")
    public String deleteComment(@PathVariable Long postId,
                                @PathVariable Long commentId,
                                HttpSession session) {

        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/user/login";

        PostCommentDto dto = commentService.findById(commentId);

        if (!dto.getEmail().equals(loginUser.getEmail())) {
            return "redirect:/interior/" + postId; // 본인 아님 → 차단
        }

        commentService.delete(commentId);
        return "redirect:/interior/" + postId;
    }

    /** 댓글 수정 */
    @PostMapping("/{postId}/comment/edit/{commentId}")
    public String editComment(@PathVariable Long postId,
                              @PathVariable Long commentId,
                              @RequestParam String content,
                              HttpSession session) {

        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/user/login";

        PostCommentDto dto = commentService.findById(commentId);

        if (!dto.getEmail().equals(loginUser.getEmail())) {
            return "redirect:/interior/" + postId;
        }

        dto.setContent(content);
        commentService.update(dto);
        return "redirect:/interior/" + postId;
    }

    /** 파일 업로드 처리 */
    private void handleMultipleFiles(InteriorPostDto dto, MultipartFile[] files) {
        if (files == null || files.length == 0) return;

        List<String>[] result = handleAndReturnFiles(files);
        dto.setFilePaths(String.join(",", result[0]));
        dto.setFileNames(String.join(",", result[1]));
    }

    private List<String>[] handleAndReturnFiles(MultipartFile[] files) {
        List<String> filePaths = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        String uploadDir = System.getProperty("user.dir") + "/uploads/";

        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    String originalName = file.getOriginalFilename();
                    String uniqueName = System.currentTimeMillis() + "_" + originalName;
                    File dest = new File(uploadDir + uniqueName);
                    file.transferTo(dest);

                    filePaths.add("/uploads/" + uniqueName);
                    fileNames.add(uniqueName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return new List[]{filePaths, fileNames};
    }


    /** 좋아요 */
    @PostMapping("/{postId}/like")
    @ResponseBody
    public String toggleLike(@PathVariable Long postId, HttpSession session) {
        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "unauthorized"; // JS에서 401 체크 대신 이걸 처리
        }

        return postLikeService.toggleLike(postId, loginUser.getEmail());
    }
}
