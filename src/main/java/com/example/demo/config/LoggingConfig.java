package com.example.demo.config;

import com.example.demo.interceptor.MonitoringInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoggingConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MonitoringInterceptor())
        .excludePathPatterns("/js/**", "/css/**", "/img/**", "/error/**",
                "/favicon.ico", "/.well-known/**", "/user/checkEmail");
    }
}
