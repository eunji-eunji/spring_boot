package com.springboot.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class Mem {
    private String name;
    private MultipartFile fileImage;
}
