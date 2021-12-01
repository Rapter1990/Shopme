package com.shopme.admin.service.impl;

import com.shopme.common.entity.Review;
import com.shopme.common.exception.ReviewNotFoundException;

public interface IReviewService {

	public Review get(Integer id) throws ReviewNotFoundException;
	public void save(Review reviewInForm);
	public void delete(Integer id) throws ReviewNotFoundException;
}
