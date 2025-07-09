package com.example.demo.repository;

import com.example.demo.constant.OrderStatus;
import com.example.demo.entity.Order;
import com.example.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByOrderId(String orderId); // 주문아이디로 찾기
    List<Order> findByUsersAndOrderStatus(Users user, OrderStatus orderStatus);//
    List<Order> findByOrderIdAndOrderStatus(String orderId, OrderStatus orderStatus);//

    List<Order> findAllByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
