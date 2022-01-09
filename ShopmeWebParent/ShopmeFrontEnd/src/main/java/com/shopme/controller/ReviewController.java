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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.common.exception.ReviewNotFoundException;
import com.shopme.service.CustomerService;
import com.shopme.service.ProductService;
import com.shopme.service.ReviewService;
import com.shopme.service.ReviewVoteService;
import com.shopme.util.AuthenticationControllerHelperUtil;

@Controller
public class ReviewController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);
	
	private String defaultRedirectURL = "redirect:/reviews/page/1?sortField=reviewTime&sortDir=desc";

	private ReviewService reviewService;
	
	private ProductService productService;
	
	private AuthenticationControllerHelperUtil authenticationControllerHelperUtil;
	
	private ReviewVoteService voteService;

	@Autowired
	public ReviewController(ReviewService reviewService, 
			                CustomerService customerService, 
			                ProductService productService,
			                AuthenticationControllerHelperUtil authenticationControllerHelperUtil,
			                ReviewVoteService voteService) {
		super();
		this.reviewService = reviewService;
		this.productService = productService;
		this.authenticationControllerHelperUtil = authenticationControllerHelperUtil;
		this.voteService = voteService;
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
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
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
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
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
	
	@GetMapping("/ratings/{productAlias}")
	public String listByProductFirstPage(@PathVariable(name = "productAlias") String productAlias, Model model,
			HttpServletRequest request) {
		
		LOGGER.info("ReviewController | listByProductFirstPage is called");
		
		return listByProductByPage(model, productAlias, 1, "reviewTime", "desc", request);
	}
	
	@GetMapping("/ratings/{productAlias}/page/{pageNum}") 
	public String listByProductByPage(Model model,
				@PathVariable(name = "productAlias") String productAlias,
				@PathVariable(name = "pageNum") int pageNum,
				String sortField, String sortDir, HttpServletRequest request) {

		LOGGER.info("ReviewController | listByProductByPage is called");
		
		LOGGER.info("ReviewController | listByProductByPage | productAlias : " + productAlias);
		LOGGER.info("ReviewController | listByProductByPage | pageNum : " + pageNum);
		LOGGER.info("ReviewController | listByProductByPage | sortField : " + sortField);
		LOGGER.info("ReviewController | listByProductByPage | sortDir : " + sortDir);
		
		Product product = null;

		try {
			product = productService.getProduct(productAlias);
		} catch (ProductNotFoundException ex) {
			LOGGER.info("ReviewController | listByProductByPage | ProductNotFoundException : " + ex.getMessage());
			return "error/404";
		}

		Page<Review> page = reviewService.listByProduct(product, pageNum, sortField, sortDir);
		List<Review> listReviews = page.getContent();
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		LOGGER.info("ReviewController | listByProductByPage | customer : " + customer.toString());
		
		if (customer != null) {
			LOGGER.info("ReviewController | listByProductByPage | customer != null : " + (customer != null));
			voteService.markReviewsVotedForProductByCustomer(listReviews, product.getId(), customer.getId());
		}
		
		LOGGER.info("ReviewController | listByProductByPage | listReviews size : " + listReviews.size());
		
		
		LOGGER.info("ReviewController | listByProductByPage | totalPages : " + page.getTotalPages());
		LOGGER.info("ReviewController | listByProductByPage | totalItems : " + page.getTotalElements());
		LOGGER.info("ReviewController | listByProductByPage | currentPage : " + pageNum);
		LOGGER.info("ReviewController | listByProductByPage | sortField : " + sortField);
		LOGGER.info("ReviewController | listByProductByPage | sortDir : " + sortDir);
		LOGGER.info("ReviewController | listByProductByPage | reverseSortDir : " + (sortDir.equals("asc") ? "desc" : "asc"));
		

		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		LOGGER.info("ReviewController | listByProductByPage | listReviews size : " + listReviews.size());
		LOGGER.info("ReviewController | listByProductByPage | product : " + product.toString());

		model.addAttribute("listReviews", listReviews);
		model.addAttribute("product", product);

		long startCount = (pageNum - 1) * ReviewService.REVIEWS_PER_PAGE + 1;
		
		LOGGER.info("ReviewController | listByProductByPage | startCount : " + startCount);
		
		model.addAttribute("startCount", startCount);

		long endCount = startCount + ReviewService.REVIEWS_PER_PAGE - 1;
		
		LOGGER.info("ReviewController | listByProductByPage | endCount : " + endCount);
		
		LOGGER.info("ReviewController | listByProductByPage | endCount : " + endCount);
		LOGGER.info("ReviewController | listByProductByPage | page.getTotalElements() : " + page.getTotalElements());
		
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		LOGGER.info("ReviewController | listByProductByPage | endCount : " + endCount);

		model.addAttribute("endCount", endCount);
		
		LOGGER.info("ReviewController | listByProductByPage | pageTitle : " + "Reviews for " + product.getShortName());
		
		model.addAttribute("pageTitle", "Reviews for " + product.getShortName());

		return "reviews/reviews_product";
	}
	
	@GetMapping("/write_review/product/{productId}")
	public String showViewForm(@PathVariable("productId") Integer productId, Model model,
			HttpServletRequest request) throws CustomerNotFoundException {
		
		LOGGER.info("ReviewController | showViewForm is called");
		LOGGER.info("ReviewController | showViewForm | productId : " + productId);
		
		Review review = new Review();

		Product product = null;

		try {
			product = productService.getProduct(productId);
			LOGGER.info("ReviewController | showViewForm | product : " + product.toString());
		} catch (ProductNotFoundException ex) {
			LOGGER.info("ReviewController | showViewForm | ProductNotFoundException (error/404): " + ex.getMessage());
			return "error/404";
		}

		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		LOGGER.info("ReviewController | showViewForm | customer : " + customer.toString());
		
		boolean customerReviewed = reviewService.didCustomerReviewProduct(customer, product.getId());
		
		LOGGER.info("ReviewController | showViewForm | customerReviewed : " + customerReviewed);

		if (customerReviewed) {
			model.addAttribute("customerReviewed", customerReviewed);
			LOGGER.info("ReviewController | showViewForm | model customerReviewed : " + customerReviewed);
		} else {
			boolean customerCanReview = reviewService.canCustomerReviewProduct(customer, product.getId());
			
			LOGGER.info("ReviewController | showViewForm | customerCanReview : " + customerCanReview);

			if (customerCanReview) {
				model.addAttribute("customerCanReview", customerCanReview);
				LOGGER.info("ReviewController | showViewForm | model customerCanReview : " + customerCanReview);
			} else {
				model.addAttribute("NoReviewPermission", true);
				LOGGER.info("ReviewController | showViewForm | model NoReviewPermission : " + true);
			}
		}		
		
		LOGGER.info("ReviewController | showViewForm | product : " + product.toString());

		model.addAttribute("product", product);
		model.addAttribute("review", review);
		
		return "reviews/review_form";
	}
	
	@PostMapping("/post_review")
	public String saveReview(Model model, Review review, Integer productId, HttpServletRequest request) throws CustomerNotFoundException {
		
		LOGGER.info("ReviewController | saveReview is called");
		LOGGER.info("ReviewController | saveReview | productId : " + productId);
		LOGGER.info("ReviewController | saveReview | review : " + review.toString());
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);

		Product product = null;

		try {
			product = productService.getProduct(productId);
			LOGGER.info("ReviewController | saveReview | product : " + product.toString());
		} catch (ProductNotFoundException ex) {
			LOGGER.info("ReviewController | saveReview | ProductNotFoundException (error/404): " + ex.getMessage());
			return "error/404";
		}

		review.setProduct(product);
		review.setCustomer(customer);

		Review savedReview = reviewService.save(review);
		
		LOGGER.info("ReviewController | saveReview | savedReview : " + savedReview.toString());

		model.addAttribute("review", savedReview);

		return "reviews/review_done";
	}
	
}
