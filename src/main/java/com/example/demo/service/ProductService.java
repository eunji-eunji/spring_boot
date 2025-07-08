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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.AccessDeniedException;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository imageRepository;
    private final WishlistRepository wishlistRepository;

    // ✅ 상품 등록
    public Long createProduct(ProductForm productForm,
                              List<MultipartFile> productImgFileList,
                              Users loginUser) throws Exception {
        log.info("상품 등록 시작: {}", productForm);

        // 1. Product 생성 및 저장
        Product product = productForm.createProduct();
        product.setUser(loginUser);
        product = productRepository.save(product);

        // 2. 모델 저장 및 재고 합산
        int totalStock = 0;
        for (ProductModelDto dto : productForm.getProductModelDtoList()) {
            ProductModel model = new ProductModel();
            model.setProductModelSelect(
                    dto.getProductModelSelect() != null ? dto.getProductModelSelect() : ProductModelSelect.DEFAULT_MODEL);
            model.setPrStock(dto.getPrStock() != null ? dto.getPrStock() : 0);
            model.setProduct(product);
            product.addProductModel(model);
            totalStock += model.getPrStock();
        }
        product.setStockQuantity(totalStock);
        productRepository.save(product);

        // 3. 이미지 업로드 및 저장 (게시판 방식 적용)
        if (productImgFileList != null && !productImgFileList.isEmpty()) {
            List<String>[] result = handleAndReturnFiles(productImgFileList.toArray(new MultipartFile[0]));
            for (String path : result[0]) {
                ProductImage img = new ProductImage();
                img.setImageUrl(path);
                img.setProduct(product);
                imageRepository.save(img);
            }
        }

        return product.getId();
    }

    // ✅ 상품 목록 조회
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
        boolean liked = user != null && wishlistRepository.existsByUserAndProduct(user, product);
        return new ProductDetailDto(product, liked);
    }

    // ✅ 상품 수정
    @Transactional
    public void updateProduct(Long productId, ProductForm form, MultipartFile[] images, String deleteIndexes, Users loginUser) {
        Product product = productRepository.findById(productId).orElseThrow();

        if (!product.getUser().getId().equals(loginUser.getId())) {
            throw new AccessDeniedException("권한 없음");
        }

        // 기존 이미지 삭제
        if (deleteIndexes != null && !deleteIndexes.isEmpty()) {
            List<ProductImage> currentImages = product.getImages();
            List<Integer> indexesToDelete = Arrays.stream(deleteIndexes.split(","))
                    .map(Integer::parseInt)
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());
            for (Integer idx : indexesToDelete) {
                if (idx >= 0 && idx < currentImages.size()) {
                    ProductImage removed = currentImages.remove((int) idx);
                    deleteFile(removed.getImageUrl());
                    imageRepository.delete(removed);
                }
            }
        }

        // 새 이미지 업로드
        if (images != null) {
            List<String>[] result = handleAndReturnFiles(images);
            for (String path : result[0]) {
                ProductImage image = new ProductImage();
                image.setImageUrl(path);
                image.setProduct(product);
                product.getImages().add(image);
            }
        }

        // 상품 정보 업데이트
        product.setName(form.getName());
        product.setPrice(form.getPrice());
        product.setCategory(form.getCategory());
        product.setDescription(form.getDescription());

        productRepository.save(product);
    }

    // ✅ 파일 업로드 처리 및 경로 반환
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

    // ✅ 파일 삭제 유틸
    private void deleteFile(String relativePath) {
        if (relativePath == null) return;
        String absolutePath = System.getProperty("user.dir") + relativePath;
        File file = new File(absolutePath);
        if (file.exists()) file.delete();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
    }

    @Transactional
    public void deleteProduct(Long id, Users user) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        if (product.getUser() == null || !product.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("상품 삭제 권한이 없습니다.");
        }

        imageRepository.deleteAll(product.getImages());
        product.getProductModels().clear();

        productRepository.delete(product);
    }
}
