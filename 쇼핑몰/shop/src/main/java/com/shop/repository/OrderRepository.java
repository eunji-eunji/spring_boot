package com.shop.repository;

import com.shop.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("select o from Order o " +
            "where o.member.email = :email " +
            "order by o.orderDate desc")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);   // ①
    // select o from Order o : order 엔티티를 o로 조회
    // where o.member.email = :email : order와 연관된 Member 엔티티의 email 필드가 
    // 주어진 파라미터와 일치하는 주문 조회
    // order by o.orderDate desc : 주문 날짜 기준 내림차순

    // 위와 같은 방법
//    @Query(value = "SELECT o.* FROM orders o " +
//            "JOIN members m ON o.member_id = m.id " +
//            "WHERE m.email = :email " +
//            "ORDER BY o.order_date DESC",
//            nativeQuery = true)
//    List<Order> findOrders(@Param("email") String email, Pageable pageable);
    
    @Query("select count(o) from Order o " +
            "where o.member.email = :email")
    Long countOrder(@Param("email") String email);                             // ②
}
