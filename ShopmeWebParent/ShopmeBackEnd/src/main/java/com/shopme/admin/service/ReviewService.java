package com.shopme.admin.service;

import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.repository.ProductRepository;
import com.shopme.admin.repository.ReviewRepository;
import com.shopme.admin.service.impl.IReviewService;
import com.shopme.common.entity.Review;
import com.shopme.common.exception.ReviewNotFoundException;

@Service
@Transactional
public class ReviewService implements IReviewService{
	
	public static final int REVIEWS_PER_PAGE = 5;

	
	private ReviewRepository reviewRepo;
	
	private ProductRepository productRepo;
	
	@Autowired 
	public ReviewService(ReviewRepository reviewRepo, ProductRepository productRepo) {
		super();
		this.reviewRepo = reviewRepo;
		this.productRepo = productRepo;
	}

	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, REVIEWS_PER_PAGE, reviewRepo);
	}

	@Override
	public Review get(Integer id) throws ReviewNotFoundException {
		// TODO Auto-generated method stub
		try {
			return reviewRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ReviewNotFoundException("Could not find any reviews with ID " + id);
		}
	}

	@Override
	public void save(Review reviewInForm) {
		// TODO Auto-generated method stub
		Review reviewInDB = reviewRepo.findById(reviewInForm.getId()).get();
		reviewInDB.setHeadline(reviewInForm.getHeadline());
		reviewInDB.setComment(reviewInForm.getComment());

		reviewRepo.save(reviewInDB);
		productRepo.updateReviewCountAndAverageRating(reviewInDB.getProduct().getId());
	}

	@Override
	public void delete(Integer id) throws ReviewNotFoundException {
		// TODO Auto-generated method stub
		if (!reviewRepo.existsById(id)) {
			throw new ReviewNotFoundException("Could not find any reviews with ID " + id);
		}

		reviewRepo.deleteById(id);
	}

}
