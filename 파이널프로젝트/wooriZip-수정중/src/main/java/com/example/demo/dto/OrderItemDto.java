package com.example.demo.dto;

import com.example.demo.entity.OrderDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OrderItemDto {

    private Long productId;
    private Long orderItemId; // 주문아이템들의 아이디
    private String productName; // 제품이름
    private Long modelId; // 모델 아이디
    private int count; // 개수
    private int price; // 가격
    private String imgUrl;

    public OrderItemDto(OrderDetail orderItem) {
        this.productId = orderItem.getProduct().getId();
        this.orderItemId = orderItem.getId();
        this.productName = orderItem.getProduct().getName();
        this.modelId =  orderItem.getProductModel().getId();
        this.count = orderItem.getCount();
        this.price = orderItem.getProduct().getPrice();
        // 대표사진만 필요
        this.imgUrl = orderItem.getProduct().getImages().get(0).getImageUrl();
    }
}
