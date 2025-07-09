package com.example.demo.service;


import com.example.demo.constant.ProductModelSelect;
import com.example.demo.dto.ProductDetailDto;
import com.example.demo.dto.ProductForm;
import com.example.demo.dto.ProductModelDto;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductImage;
import com.example.demo.entity.ProductModel;
import com.example.demo.entity.Users;
import com.example.demo.repository.ProductImageRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository imageRepository;
    private final WishlistRepository wishlistRepository;

    // ✅ 상품 등록
    public Long createProduct(ProductForm productForm, List<MultipartFile> productImgFileList) throws Exception {
        log.info("상품 등록 시작: {}", productForm);

        // 1. Product 엔티티 생성 및 저장
        Product product = productForm.createProduct();
        product = productRepository.save(product);
        productRepository.flush();

        log.info("상품 저장 완료: {}", product);

        // 2. 모델 정보 저장 및 재고 합산
        int totalStock = 0;
        for (ProductModelDto dto : productForm.getProductModelDtoList()) {
            ProductModel model = new ProductModel();
            model.setProductModelSelect(
                    dto.getProductModelSelect() != null ? dto.getProductModelSelect() : ProductModelSelect.DEFAULT_MODEL
            );
            model.setPrStock(dto.getPrStock() != null ? dto.getPrStock() : 0);
            model.setProduct(product);
            product.addProductModel(model);
            totalStock += model.getPrStock();

            log.info("모델 추가 완료: {}", model);
        }

        product.setStockQuantity(totalStock);
        productRepository.save(product);

        log.info("총 재고 수량 설정 완료: {}", totalStock);

        // 3. 이미지 저장 처리
        for (MultipartFile file : productImgFileList) {
            String imageUrl = saveImage(file); // 로컬 또는 S3 구현 예정
            ProductImage img = new ProductImage();
            img.setImageUrl(imageUrl);
            img.setProduct(product);
            imageRepository.save(img);
        }

        return product.getId();
    }

    // ✅ 상품 목록 조회 (카테고리 필터 포함)
    public List<Product> findProducts(String category) {
        if (category != null && !category.isEmpty()) {
            return productRepository.findByCategory(category);
        }
        return productRepository.findAll();
    }

    // ✅ 상품 상세 조회 (찜 여부 포함)
    public ProductDetailDto getProductDetail(Long productId, Users user) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        //boolean liked = wishlistRepository.existsByUserAndProduct(user, product);
        // null검사 추가 나중에 제거
        boolean liked = false;
        if (user != null) {
            liked = wishlistRepository.existsByUserAndProduct(user, product);
        }

        return new ProductDetailDto(product, liked);
    }

    // ✅ 이미지 저장 메서드
    private String saveImage(MultipartFile file) {
        if (file.isEmpty()) return null;

        try {
            // ✅ 프로젝트 경로에 uploads 폴더 만들기
            String uploadDir = new File("uploads").getAbsolutePath() + File.separator;

            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String originalName = file.getOriginalFilename();
            String uniqueName = System.currentTimeMillis() + "_" + originalName;
            File dest = new File(uploadDir + uniqueName);
            file.transferTo(dest);

            // ✅ 웹 접근용 경로 반환
            return "/uploads/" + uniqueName;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
