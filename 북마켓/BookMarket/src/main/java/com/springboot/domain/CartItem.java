package com.springboot.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
public class CartItem implements Serializable {
    private static final long serialVersionUID = 3636831123198280235L;
    private Long id;
    private Book book;
    private int quantity;
    private BigDecimal totalPrice;
    public CartItem(Book book){
        super();
        this.book=book;
        this.quantity=1;
        this.totalPrice=book.getUnitPrice();
    }

    public void setBook(Book book){
        this.book=book;
        this.updateTotalPrice();    // 책 변경 시 총 가격 다시 계산
    }

    private void updateTotalPrice() {
        totalPrice=this.book.getUnitPrice().multiply(new BigDecimal(this.quantity));
    }

    public void setQuantity(int quantity){
        this.quantity=quantity;
        this.updateTotalPrice();    // 수량 변경 시 총 가격 다시 계산
    }
}
