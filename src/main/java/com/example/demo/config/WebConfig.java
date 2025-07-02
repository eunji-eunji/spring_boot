package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 절대 경로로 매핑해주기
        String uploadPath = Paths.get(System.getProperty("user.dir"), "uploads").toUri().toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);  // file:/C:/.../uploads/
    }
}
