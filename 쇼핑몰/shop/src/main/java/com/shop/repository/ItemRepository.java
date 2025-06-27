package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>,
        QuerydslPredicateExecutor<Item>, ItemRepositoryCustom{
    List<Item> findByItemNm(String itemNm);
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
    List<Item> findByPriceLessThan(Integer price);
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    // jpql - entity에 있는 변수명 써야함
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    // select * from item where item_detail like "%테스트%" order by price desc;
    // 순수 쿼리 - sql의 칼럼명으로 씀
//    @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by price desc", nativeQuery = true)
//    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

//    SELECT * FROM item WHERE item_detail LIKE '%테스트 상품 상세 설명%' AND price > 10003 AND item_sell_status = 'SELL' ORDER BY price DESC;

}
