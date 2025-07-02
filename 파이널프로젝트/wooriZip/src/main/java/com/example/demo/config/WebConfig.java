package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ✅ 프로젝트 내부 uploads 폴더 기준 매핑
        String uploadPath = Paths.get("uploads").toAbsolutePath().toUri().toString();
        // 예: file:///C:/Users/username/project/uploads/

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }
}
