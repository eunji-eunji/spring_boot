package com.example.demo.config;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CategoryInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            // 1. 대분류 "가구"
            Category furniture = Category.builder()
                    .name("가구")
                    .depth(0)
                    .parent(null)
                    .build();
            categoryRepository.save(furniture);

            // 2. 소분류들 + 세분류 포함 구조
            Map<String, List<String>> subWithChildren = Map.of(
                    "침대", List.of("슈퍼싱글", "퀸", "킹"),
                    "소파", List.of("1인용", "2~3인용", "카우치형"),
                    "테이블·식탁·책상", List.of("식탁", "좌식테이블", "사무용책상"),
                    "수납·거실장", List.of("서랍장", "TV장", "수납장"),
                    "의자", List.of("식탁의자", "사무용의자", "스툴"),
                    "행거·옷장", List.of("행거", "붙박이장")

            );

            for (Map.Entry<String, List<String>> entry : subWithChildren.entrySet()) {
                String name = entry.getKey();
                List<String> children = entry.getValue();

                // 소분류 생성
                Category mid = Category.builder()
                        .name(name)
                        .depth(1)
                        .parent(furniture)
                        .build();
                categoryRepository.save(mid);

                // 세분류 생성
                for (String childName : children) {
                    Category leaf = Category.builder()
                            .name(childName)
                            .depth(2)
                            .parent(mid)
                            .build();
                    categoryRepository.save(leaf);
                }
            }

            // 3. 기타 소분류 (세분류 없음)
            List<String> etcMids = List.of("선반·진열장·책장", "거울");
            for (String name : etcMids) {
                Category mid = Category.builder()
                        .name(name)
                        .depth(1)
                        .parent(furniture)
                        .build();
                categoryRepository.save(mid);
            }
        }
    }



}

