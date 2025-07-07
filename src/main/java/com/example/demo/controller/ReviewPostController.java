package com.example.demo.controller;

import com.example.demo.dto.ReviewPostDto;
import com.example.demo.entity.Users;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.ReviewPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/review")
public class ReviewPostController {

    private final ReviewPostService service;

    /** 리뷰 글 목록 + 페이지네이션 */
    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int page,
                       Model model,
                       @AuthenticationPrincipal CustomUserDetails userDetails) {

        Users loginUser = (userDetails != null) ? userDetails.getUser() : null;

        int pageSize = 10;
        Page<ReviewPostDto> postPage = service.findPagedPosts(page, pageSize);

        model.addAttribute("postPage", postPage);
        model.addAttribute("postList", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("loginUser", loginUser);

        return "review/list";
    }

    /** 리뷰 작성 폼 */
    @GetMapping("/write")
    public String writeForm(Model model,
                            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Users loginUser = (userDetails != null) ? userDetails.getUser() : null;
        if (loginUser == null) return "redirect:/user/login";

        model.addAttribute("dto", new ReviewPostDto());
        model.addAttribute("productList", service.getAllProducts());
        model.addAttribute("loginUser", loginUser);
        return "review/write";
    }

    /** 리뷰 작성 처리 */
    @PostMapping("/write")
    @ResponseBody
    public String writePost(@ModelAttribute ReviewPostDto dto,
                            @RequestParam(value = "files", required = false) MultipartFile[] files,
                            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Users loginUser = (userDetails != null) ? userDetails.getUser() : null;
        if (loginUser == null) return "unauthorized";

        if (files != null && files.length > 0 && !files[0].isEmpty()) {
            handleMultipleFiles(dto, files);
        }

        service.save(dto, loginUser);
        return "success";
    }

    /** 리뷰 상세 */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         Model model,
                         @AuthenticationPrincipal CustomUserDetails userDetails) {

        Users loginUser = (userDetails != null) ? userDetails.getUser() : null;

        ReviewPostDto dto = service.findById(id);
        model.addAttribute("dto", dto);
        model.addAttribute("loginUser", loginUser);
        return "review/detail";
    }

    /** 리뷰 수정 폼 */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id,
                           Model model,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {

        Users loginUser = (userDetails != null) ? userDetails.getUser() : null;
        if (loginUser == null) return "redirect:/user/login";

        ReviewPostDto dto = service.findById(id);
        model.addAttribute("dto", dto);
        model.addAttribute("productList", service.getAllProducts());
        model.addAttribute("loginUser", loginUser);
        return "review/edit";
    }

    /** 리뷰 수정 처리 */
    @PostMapping("/edit")
    @ResponseBody
    public String editPost(@ModelAttribute ReviewPostDto dto,
                           @RequestParam(value = "files", required = false) MultipartFile[] files,
                           @RequestParam(value = "deleteIndexes", required = false) List<Integer> deleteIndexes,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {

        Users loginUser = (userDetails != null) ? userDetails.getUser() : null;
        if (loginUser == null) return "unauthorized";

        // 기존 이미지 리스트 유지하면서 삭제 인덱스 제거
        List<String> filePathList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        if (dto.getFilePaths() != null && !dto.getFilePaths().isEmpty()) {
            filePathList = new ArrayList<>(List.of(dto.getFilePaths().split(",")));
        }
        if (dto.getFileNames() != null && !dto.getFileNames().isEmpty()) {
            fileNameList = new ArrayList<>(List.of(dto.getFileNames().split(",")));
        }

        if (deleteIndexes != null && !deleteIndexes.isEmpty()) {
            deleteIndexes.sort(Comparator.reverseOrder());
            for (int index : deleteIndexes) {
                if (index >= 0 && index < filePathList.size()) {
                    File file = new File(System.getProperty("user.dir") + filePathList.get(index));
                    if (file.exists()) file.delete();
                    filePathList.remove(index);
                    if (index < fileNameList.size()) fileNameList.remove(index);
                }
            }
        }

        // 새 이미지 업로드
        List<String> addedPaths = new ArrayList<>();
        List<String> addedNames = new ArrayList<>();
        if (files != null && files.length > 0 && !files[0].isEmpty()) {
            List<String>[] result = handleAndReturnFiles(files);
            addedPaths = result[0];
            addedNames = result[1];
        }

        filePathList.addAll(addedPaths);
        fileNameList.addAll(addedNames);

        dto.setFilePaths(String.join(",", filePathList));
        dto.setFileNames(String.join(",", fileNameList));

        service.save(dto, loginUser);
        return "success";
    }

    /** 리뷰 삭제 */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         @AuthenticationPrincipal CustomUserDetails userDetails) {

        Users loginUser = (userDetails != null) ? userDetails.getUser() : null;
        if (loginUser == null) return "redirect:/user/login";

        service.delete(id);
        return "redirect:/review";
    }

    /** 상품 리뷰 프래그먼트 */
    @GetMapping("/fragment/{productId}")
    public String reviewFragment(@PathVariable Long productId,
                                 Model model,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {

        Users loginUser = (userDetails != null) ? userDetails.getUser() : null;

        List<ReviewPostDto> reviews = service.findByProductId(productId);
        boolean hasWrittenReview = (loginUser != null) && service.hasUserReviewedProduct(loginUser, productId);

        model.addAttribute("productId", productId);
        model.addAttribute("reviews", reviews);
        model.addAttribute("hasWrittenReview", hasWrittenReview);
        model.addAttribute("isLoggedIn", loginUser != null);
        model.addAttribute("loginUser", loginUser);

        return "review/review :: reviewBoard";
    }

    /** 파일 업로드 처리 */
    private void handleMultipleFiles(ReviewPostDto dto, MultipartFile[] files) {
        if (files == null || files.length == 0) return;

        List<String>[] result = handleAndReturnFiles(files);
        dto.setFilePaths(String.join(",", result[0]));
        dto.setFileNames(String.join(",", result[1]));
    }

    /** 파일 업로드 처리 및 경로 반환 */
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
}
