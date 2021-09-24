package com.shopme.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopme.common.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
