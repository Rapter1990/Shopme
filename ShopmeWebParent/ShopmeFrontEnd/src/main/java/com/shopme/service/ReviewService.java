package com.shopme.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ReviewNotFoundException;
import com.shopme.repository.ReviewRepository;
import com.shopme.service.impl.IReviewService;

@Service
@Transactional
public class ReviewService implements IReviewService {

	public static final int REVIEWS_PER_PAGE = 5;

	@Autowired 
	private ReviewRepository repo;
	
	@Override
	public Page<Review> listByCustomerByPage(Customer customer, String keyword, int pageNum, String sortField,
			String sortDir) {
		
		// TODO Auto-generated method stub
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, REVIEWS_PER_PAGE, sort);

		if (keyword != null) {
			return repo.findByCustomer(customer.getId(), keyword, pageable);
		}

		return repo.findByCustomer(customer.getId(), pageable);
	}

	@Override
	public Review getByCustomerAndId(Customer customer, Integer reviewId) throws ReviewNotFoundException {
		// TODO Auto-generated method stub
		Review review = repo.findByCustomerAndId(customer.getId(), reviewId);
		if (review == null) 
			throw new ReviewNotFoundException("Customer doesn not have any reviews with ID " + reviewId);

		return review;
	}
	
	public Page<Review> list3MostRecentReviewsByProduct(Product product) {
		Sort sort = Sort.by("reviewTime").descending();
		Pageable pageable = PageRequest.of(0, 3, sort);

		return repo.findByProduct(product, pageable);		
	}

}
