package com.shopme.service.impl;

import org.springframework.data.domain.Page;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.exception.ReviewNotFoundException;

public interface IReviewService {

	public Page<Review> listByCustomerByPage(Customer customer, String keyword, int pageNum, 
			String sortField, String sortDir);
	
	public Review getByCustomerAndId(Customer customer, Integer reviewId) throws ReviewNotFoundException;
}
