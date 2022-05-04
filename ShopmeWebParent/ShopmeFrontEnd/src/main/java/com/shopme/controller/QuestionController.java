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
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.question.Question;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.service.ProductService;
import com.shopme.service.QuestionService;
import com.shopme.service.QuestionVoteService;
import com.shopme.util.AuthenticationControllerHelperUtil;

@Controller
public class QuestionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);
	
	private QuestionService questionService;
	
	private ProductService productService;
	
	private AuthenticationControllerHelperUtil authenticationControllerHelperUtil;
	
	private QuestionVoteService voteService;
	
	@Autowired
	public QuestionController(QuestionService questionService, ProductService productService,
			AuthenticationControllerHelperUtil authenticationControllerHelperUtil,
			QuestionVoteService voteService) {
		super();
		this.questionService = questionService;
		this.productService = productService;
		this.authenticationControllerHelperUtil = authenticationControllerHelperUtil;
		this.voteService = voteService;
	}

	@GetMapping("/questions/{productAlias}") 
	public String listQuestionsOfProduct(@PathVariable(name = "productAlias") String productAlias,
			Model model, HttpServletRequest request) throws ProductNotFoundException {
		
		LOGGER.info("QuestionController | listQuestionsOfProduct is called");
		
		return listQuestionsOfProductByPage(model, request, productAlias, 1, "votes", "desc");
	}
	
	@GetMapping("/questions/{productAlias}/page/{pageNum}") 
	public String listQuestionsOfProductByPage(
				Model model, HttpServletRequest request,
				@PathVariable(name = "productAlias") String productAlias,
				@PathVariable(name = "pageNum") int pageNum,
				String sortField, String sortDir) throws ProductNotFoundException {
		
		LOGGER.info("QuestionController | listQuestionsOfProductByPage is called");
		
		Page<Question> page = questionService.listQuestionsOfProduct(productAlias, pageNum, sortField, sortDir);
		List<Question> listQuestions = page.getContent();
		Product product = productService.getProduct(productAlias);
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | customer : " + customer.getFullName());
		
		if (customer != null) {
			voteService.markQuestionsVotedForProductByCustomer(listQuestions, product.getId(), customer.getId());
		}	
		

		LOGGER.info("QuestionController | listQuestionsOfProductByPage | totalPages : " + page.getTotalPages());
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | totalItems : " + page.getTotalElements());
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | currentPage : " + pageNum);
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | sortField : " + sortField);
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | sortDir : " + sortDir);
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | reverseSortDir : " + (sortDir.equals("asc") ? "desc" : "asc"));
		
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | listQuestions size : " + listQuestions.size());
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | product name : " + product.getName());
		
		model.addAttribute("listQuestions", listQuestions);
		model.addAttribute("product", product);

		long startCount = (pageNum - 1) * QuestionService.QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING + 1;
		model.addAttribute("startCount", startCount);
		
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | startCount : " + startCount);

		long endCount = startCount + QuestionService.QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING - 1;
		
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | endCount : " + endCount);
		
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | endCount : " + endCount);
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | page.getTotalElements() : " + page.getTotalElements());
		
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | endCount > page.getTotalElements() : "
		+ (endCount > page.getTotalElements()));
		
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		model.addAttribute("endCount", endCount);
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | endCount : " + endCount);
		
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | page.getTotalPages() : " + page.getTotalPages());

		if (page.getTotalPages() > 1) {
			LOGGER.info("QuestionController | listQuestionsOfProductByPage | pageTitle : " + "Page " + pageNum + " | Questions for product: " + product.getName());
			model.addAttribute("pageTitle", "Page " + pageNum + " | Questions for product: " + product.getName());
		} else {
			LOGGER.info("QuestionController | listQuestionsOfProductByPage | pageTitle : " + "Questions for product: " + product.getName());
			model.addAttribute("pageTitle", "Questions for product: " + product.getName());
		}		

		return "product/product_questions";
	}
	
	@GetMapping("/customer/questions") 
	public String listQuestionsByCustomer(Model model, HttpServletRequest request) {
		
		LOGGER.info("QuestionController | listQuestionsByCustomer is called");
		
		return listQuestionsByCustomerByPage(model, request, 1, null, "askTime", "desc");
	}
	
	@GetMapping("/customer/questions/page/{pageNum}") 
	public String listQuestionsByCustomerByPage(
				Model model, HttpServletRequest request,
				@PathVariable(name = "pageNum") int pageNum,
				String keyword, String sortField, String sortDir) {
		
		LOGGER.info("QuestionController | listQuestionsByCustomerByPage is called");
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		LOGGER.info("QuestionController | listQuestionsByCustomerByPage | customer Full Name : " + customer.getFullName());
		
		Page<Question> page = questionService.listQuestionsByCustomer(customer, keyword, pageNum, sortField, sortDir);		
		List<Question> listQuestions = page.getContent();
		
		LOGGER.info("QuestionController | listQuestionsByCustomerByPage | totalPages : " + page.getTotalPages());
		LOGGER.info("QuestionController | listQuestionsByCustomerByPage | totalItems : " + page.getTotalElements());
		LOGGER.info("QuestionController | listQuestionsByCustomerByPage | currentPage : " + pageNum);
		LOGGER.info("QuestionController | listQuestionsByCustomerByPage | sortField : " + sortField);
		LOGGER.info("QuestionController | listQuestionsByCustomerByPage | sortDir : " + sortDir);
		LOGGER.info("QuestionController | listQuestionsByCustomerByPage | keyword : " + keyword);
		LOGGER.info("QuestionController | listQuestionsByCustomerByPage | reverseSortDir : " + (sortDir.equals("asc") ? "desc" : "asc"));
		LOGGER.info("QuestionController | listQuestionsByCustomerByPage | moduleURL : " + "/customer/questions");
		
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("moduleURL", "/customer/questions");

		
		LOGGER.info("QuestionController | listQuestionsByCustomerByPage | listQuestions size : " + listQuestions.size());		
		model.addAttribute("listQuestions", listQuestions);

		long startCount = (pageNum - 1) * QuestionService.QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING + 1;		
		LOGGER.info("QuestionController | listQuestionsByCustomerByPage | startCount : " + startCount);
		
		model.addAttribute("startCount", startCount);
			
		long endCount = startCount + QuestionService.QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING - 1;
		
		LOGGER.info("QuestionController | listQuestionsByCustomerByPage | endCount : " + endCount);
		LOGGER.info("QuestionController | listQuestionsByCustomerByPage | page.getTotalElements() : " + page.getTotalElements());
		
		LOGGER.info("QuestionController | listQuestionsByCustomerByPage | endCount > page.getTotalElements() : "
		+ (endCount > page.getTotalElements()));

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		LOGGER.info("QuestionController | listQuestionsByCustomerByPage | endCount : " + endCount);
		model.addAttribute("endCount", endCount);
		
		return "question/customer_questions";
	}
	
	@GetMapping("/customer/questions/detail/{id}")
	public String viewQuestion(@PathVariable("id") Integer id, Model model, RedirectAttributes ra, 
			HttpServletRequest request) {
		
		LOGGER.info("QuestionController | viewQuestion is called");
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		LOGGER.info("QuestionController | viewQuestion | customer : " + customer.getFullName());
		
		Question question = questionService.getByCustomerAndId(customer, id);
		
		LOGGER.info("QuestionController | viewQuestion | question : " + question.getQuestionContent());

		if (question != null) {	
			model.addAttribute("question", question);
			
			LOGGER.info("QuestionController | viewQuestion | question/question_detail_modal");
			
			return "question/question_detail_modal";
		} else {
			
			LOGGER.info("QuestionController | viewQuestion | message : " + "Could not find any question with ID " + id);
			
			ra.addFlashAttribute("message", "Could not find any question with ID " + id);
			
			LOGGER.info("QuestionController | viewQuestion | redirect:/customer/questions");
			
			return "redirect:/customer/questions";			
		}
	}
	
	@GetMapping("/ask_question/{productAlias}")
	public String askQuestion(@PathVariable(name = "productAlias") String productAlias) {
		
		LOGGER.info("QuestionController | askQuestion is called");
		
		LOGGER.info("QuestionController | askQuestion | " + "redirect:/p/" + productAlias + "#qa");
		
		return "redirect:/p/" + productAlias + "#qa";
	}
}
