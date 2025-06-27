package com.springboot.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Product {
    @NotEmpty
    @Size(min=4, max=10, message="4자~10자 이내로 입력")
    private String name;

    @Min(value=0)
    private int price;
}
