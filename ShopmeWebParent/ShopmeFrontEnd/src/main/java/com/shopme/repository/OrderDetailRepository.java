package com.shopme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

	@Query("SELECT COUNT(d) FROM OrderDetail d JOIN OrderTrack t ON d.order.id = t.order.id"
			+ " WHERE d.product.id = ?1 AND d.order.customer.id = ?2 AND"
			+ " t.status = ?3")
	public Long countByProductAndCustomerAndOrderStatus(
			Integer productId, Integer customerId, OrderStatus status);
	
}
