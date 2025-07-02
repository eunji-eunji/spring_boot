package com.example.demo.dto;


import com.example.demo.constant.ProductModelSelect;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductModelDto {
    private ProductModelSelect productModelSelect; // 예: SMALL, MEDIUM, LARGE
    private Integer prStock; // 기종별 재고 수량
}
