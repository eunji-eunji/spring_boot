package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemDto {
    private Long id;
    private String itemNm;
    private Integer price;
    private String itemDetail;
    private String sellStatCd;
    private LocalDateTime regTime;
}
