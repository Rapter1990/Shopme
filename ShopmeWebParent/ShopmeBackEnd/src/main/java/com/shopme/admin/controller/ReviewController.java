package com.shopme.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.service.ReviewService;
import com.shopme.common.entity.Review;
import com.shopme.common.exception.ReviewNotFoundException;

@Controller
public class ReviewController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

	private String defaultRedirectURL = "redirect:/reviews/page/1?sortField=reviewTime&sortDir=desc";

	@Autowired 
	private ReviewService service;

	@GetMapping("/reviews")
	public String listFirstPage(Model model) {
		
		LOGGER.info("ReviewController | listFirstPage is started");
		
		return defaultRedirectURL;
	}

	@GetMapping("/reviews/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listReviews", moduleURL = "/reviews") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum) {

		LOGGER.info("ReviewController | listByPage is started");
		
		service.listByPage(pageNum, helper);

		return "reviews/reviews";
	}
	
	@GetMapping("/reviews/detail/{id}")
	public String viewReview(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		
		LOGGER.info("ReviewController | viewReview is started");
		
		LOGGER.info("ReviewController | viewReview | id : " + id);
		
		try {
			Review review = service.get(id);
			
			LOGGER.info("ReviewController | viewReview | review : " + review.toString());
			
			model.addAttribute("review", review);

			return "reviews/review_detail_modal";
		} catch (ReviewNotFoundException ex) {
			
			LOGGER.info("ReviewController | viewReview | messageError : " + ex.getMessage());
			
			ra.addFlashAttribute("messageError", ex.getMessage());
			return defaultRedirectURL;		
		}
	}
	
	@GetMapping("/reviews/edit/{id}")
	public String editReview(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		
		LOGGER.info("ReviewController | editReview is started");
		
		LOGGER.info("ReviewController | editReview | id : " + id);
		
		
		try {
			Review review = service.get(id);
			
			LOGGER.info("ReviewController | editReview | review : " + review.toString());
			LOGGER.info("ReviewController | editReview | pageTitle : " + (String.format("Edit Review (ID: %d)", id)));

			model.addAttribute("review", review);
			model.addAttribute("pageTitle", String.format("Edit Review (ID: %d)", id));

			return "reviews/review_form";
		} catch (ReviewNotFoundException ex) {
			
			LOGGER.info("ReviewController | viewReview | messageError : " + ex.getMessage());
			
			ra.addFlashAttribute("messageError", ex.getMessage());
			return defaultRedirectURL;		
		}
	}	

	@PostMapping("/reviews/save")
	public String saveReview(Review reviewInForm, RedirectAttributes ra) {
		
		LOGGER.info("ReviewController | saveReview is started");
		
		service.save(reviewInForm);		
		
		LOGGER.info("ReviewController | viewReview | messageSuccess : " + "The review ID " + reviewInForm.getId() + " has been updated successfully.");
		
		ra.addFlashAttribute("messageSuccess", "The review ID " + reviewInForm.getId() + " has been updated successfully.");
		return defaultRedirectURL;		
	}

	@GetMapping("/reviews/delete/{id}")
	public String deleteReview(@PathVariable("id") Integer id, RedirectAttributes ra) {
		
		LOGGER.info("ReviewController | deleteReview is started");
		
		LOGGER.info("ReviewController | deleteReview | id : " + id);
		
		try {
			service.delete(id);
			
			LOGGER.info("ReviewController | viewReview | messageSuccess : " + "The review ID " + id + " has been deleted.");
			
			ra.addFlashAttribute("messageSuccess", "The review ID " + id + " has been deleted.");
		} catch (ReviewNotFoundException ex) {
			
			LOGGER.info("ReviewController | viewReview | messageError : " + ex.getMessage());
			
			ra.addFlashAttribute("messageError", ex.getMessage());
		}

		return defaultRedirectURL;		
	}
}
