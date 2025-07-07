package com.example.demo.service;

import com.example.demo.dto.ReviewPostDto;
import com.example.demo.entity.Product;
import com.example.demo.entity.ReviewPost;
import com.example.demo.entity.Users;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewPostRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewPostService {

    private final ReviewPostRepository reviewRepository;
    private final ProductRepository productRepository;

    /** 글 저장 */
    @Transactional
    public void save(ReviewPostDto dto, Users loginUser) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        // 신규 작성 시에만 중복 체크
        if (dto.getId() == null) {
            if (reviewRepository.existsByUserAndProduct(loginUser, product)) {
                throw new IllegalStateException("이미 이 상품에 대한 리뷰를 작성했습니다.");
            }
        }

        ReviewPost post;
        if (dto.getId() != null) {
            // 수정인 경우 기존 글 가져오기
            post = reviewRepository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("글 없음"));
        } else {
            // 새 글 작성
            post = new ReviewPost();
            post.setUser(loginUser);
            post.setProduct(product);
        }

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setFilePaths(dto.getFilePaths());
        post.setFileNames(dto.getFileNames());
        post.setRating(dto.getRating());

        reviewRepository.save(post);
    }



    /** 단건 조회 */
    public ReviewPostDto findById(Long id) {
        ReviewPost post = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("글 없음"));
        return ReviewPostDto.fromEntity(post);
    }

    /** 전체 리스트 조회 */
    public List<ReviewPostDto> findAll() {
        return reviewRepository.findAll()
                .stream()
                .map(ReviewPostDto::fromEntity)
                .collect(Collectors.toList());
    }

    /** 삭제 */
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }

    /** 상품 리스트 조회 */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /** 페이지네이션 */
    public Page<ReviewPostDto> findPagedPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        return reviewRepository.findAll(pageable).map(ReviewPostDto::fromEntity);
    }

    /** 특정 상품의 리뷰 리스트 조회 */
    public List<ReviewPostDto> findByProductId(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(ReviewPostDto::fromEntity)
                .collect(Collectors.toList());
    }

    /** 특정 상품에 대해 로그인 사용자가 이미 리뷰 작성했는지 여부 */
    public boolean hasUserReviewedProduct(Users user, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));
        return reviewRepository.existsByUserAndProduct(user, product);
    }

    /** 정렬 + 페이징 처리 */
    public Page<ReviewPostDto> findPagedByProductSorted(Long productId, int page, int size, String sortType) {
        Sort sort = switch (sortType) {
            case "rating" -> Sort.by(Sort.Direction.DESC, "rating");
            case "latest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return reviewRepository.findByProductId(productId, pageable)
                .map(ReviewPostDto::fromEntity);
    }

    /** 평균 평점 */
    public double getAverageRating(Long productId) {
        List<ReviewPost> posts = reviewRepository.findByProductId(productId);
        if (posts.isEmpty()) return 0.0;

        double sum = posts.stream().mapToInt(ReviewPost::getRating).sum();
        return Math.round((sum / posts.size()) * 10.0) / 10.0; // 소수점 1자리
    }

    /** 리뷰 총 개수 */
    public long getReviewCount(Long productId) {
        return reviewRepository.countByProductId(productId);
    }

    /** 점수별 분포 */
    public Map<Integer, Long> getRatingDistribution(Long productId) {
        List<ReviewPost> posts = reviewRepository.findByProductId(productId);

        Map<Integer, Long> counts = posts.stream()
                .collect(Collectors.groupingBy(ReviewPost::getRating, Collectors.counting()));

        // 1~5점 모두 포함되도록 초기값 보정
        for (int i = 1; i <= 5; i++) {
            counts.putIfAbsent(i, 0L);
        }

        return counts;
    }


}
