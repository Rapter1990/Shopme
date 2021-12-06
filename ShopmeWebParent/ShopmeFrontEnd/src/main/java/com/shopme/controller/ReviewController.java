package com.shopme.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.common.exception.ReviewNotFoundException;
import com.shopme.service.CustomerService;
import com.shopme.service.ReviewService;
import com.shopme.util.CustomerShoppingCartAddressShippingOrderReviewUtil;

@Controller
public class ReviewController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);
	
	private String defaultRedirectURL = "redirect:/reviews/page/1?sortField=reviewTime&sortDir=desc";

	private ReviewService reviewService;
	
	private CustomerService customerService;

	@Autowired
	public ReviewController(ReviewService reviewService, CustomerService customerService) {
		super();
		this.reviewService = reviewService;
		this.customerService = customerService;
	}
	
	@GetMapping("/reviews")
	public String listFirstPage(Model model) {
		
		LOGGER.info("ReviewController | listFirstPage is called");
		
		return defaultRedirectURL;
	}
	
	@GetMapping("/reviews/page/{pageNum}") 
	public String listReviewsByCustomerByPage(Model model, HttpServletRequest request,
							@PathVariable(name = "pageNum") int pageNum,
							String keyword, String sortField, String sortDir) throws CustomerNotFoundException {
		
		LOGGER.info("ReviewController | listReviewsByCustomerByPage is called");
		
		Customer customer = CustomerShoppingCartAddressShippingOrderReviewUtil.getAuthenticatedCustomer(request,customerService);
		
		LOGGER.info("ReviewController | listReviewsByCustomerByPage | customer : " + customer.toString());
		
		Page<Review> page = reviewService.listByCustomerByPage(customer, keyword, pageNum, sortField, sortDir);		
		List<Review> listReviews = page.getContent();
		
		LOGGER.info("ReviewController | listReviewsByCustomerByPage | listReviews : " + listReviews.size());
		
		LOGGER.info("ReviewController | listReviewsByCustomerByPage | totalPages : " + page.getTotalPages());
		LOGGER.info("ReviewController | listReviewsByCustomerByPage | totalItems : " + page.getTotalElements());
		LOGGER.info("ReviewController | listReviewsByCustomerByPage | currentPage : " + pageNum);
		LOGGER.info("ReviewController | listReviewsByCustomerByPage | sortField : " + sortField);
		LOGGER.info("ReviewController | listReviewsByCustomerByPage | sortDir : " + sortDir);
		LOGGER.info("ReviewController | listReviewsByCustomerByPage | keyword : " + keyword);
		LOGGER.info("ReviewController | listReviewsByCustomerByPage | reverseSortDir : " + (sortDir.equals("asc") ? "desc" : "asc"));
		LOGGER.info("ReviewController | listReviewsByCustomerByPage | moduleURL : " + "/reviews");

		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("moduleURL", "/reviews");

		model.addAttribute("listReviews", listReviews);

		long startCount = (pageNum - 1) * ReviewService.REVIEWS_PER_PAGE + 1;
		
		LOGGER.info("ReviewController | listReviewsByCustomerByPage | startCount : " + startCount);
		
		model.addAttribute("startCount", startCount);

		long endCount = startCount + ReviewService.REVIEWS_PER_PAGE - 1;
		
		LOGGER.info("ReviewController | listReviewsByCustomerByPage | endCount : " + endCount);
		LOGGER.info("ReviewController | listReviewsByCustomerByPage | page.getTotalElements() : " + page.getTotalElements());
		LOGGER.info("ReviewController | listReviewsByCustomerByPage | endCount > page.getTotalElements() : " + (endCount > page.getTotalElements()));
		
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		model.addAttribute("endCount", endCount);
		
		LOGGER.info("ReviewController | listReviewsByCustomerByPage | endCount : " + endCount);

		return "reviews/reviews_customer";
	}
	
	@GetMapping("/reviews/detail/{id}")
	public String viewReview(@PathVariable("id") Integer id, Model model, 
			RedirectAttributes ra, HttpServletRequest request) throws CustomerNotFoundException {
		
		LOGGER.info("ReviewController | viewReview is called");
		
		Customer customer = CustomerShoppingCartAddressShippingOrderReviewUtil.getAuthenticatedCustomer(request,customerService);
		
		LOGGER.info("ReviewController | viewReview | customer : " + customer.toString());
		
		LOGGER.info("ReviewController | viewReview | id : " + id);
		
		try {
			
			Review review = reviewService.getByCustomerAndId(customer, id);
			
			LOGGER.info("ReviewController | viewReview | review : " + review.toString());
			
			model.addAttribute("review", review);

			return "reviews/review_detail_modal";
			
		} catch (ReviewNotFoundException ex) {
			
			LOGGER.info("ReviewController | viewReview | messageError : " + ex.getMessage());
			
			ra.addFlashAttribute("messageError", ex.getMessage());
			
			return defaultRedirectURL;		
		}
	}	
}
