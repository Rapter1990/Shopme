package com.shopme.util;

import java.util.Iterator;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.product.Product;
import com.shopme.service.ReviewService;

public class ReviewStatusUtil {

	public static void setProductReviewableStatus(Customer customer, Order order,ReviewService reviewService) {
		Iterator<OrderDetail> iterator = order.getOrderDetails().iterator();

		while(iterator.hasNext()) {
			OrderDetail orderDetail = iterator.next();
			Product product = orderDetail.getProduct();
			Integer productId = product.getId();

			boolean didCustomerReviewProduct = reviewService.didCustomerReviewProduct(customer, productId);
			product.setReviewedByCustomer(didCustomerReviewProduct);

			if (!didCustomerReviewProduct) {
				boolean canCustomerReviewProduct = reviewService.canCustomerReviewProduct(customer, productId);
				product.setCustomerCanReview(canCustomerReviewProduct);
			}

		}
	}
}
