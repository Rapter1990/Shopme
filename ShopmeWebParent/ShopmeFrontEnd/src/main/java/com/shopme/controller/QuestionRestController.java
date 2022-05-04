package com.shopme.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.question.Question;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.service.QuestionService;
import com.shopme.util.AuthenticationControllerHelperUtil;

@RestController
public class QuestionRestController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionRestController.class);

	private AuthenticationControllerHelperUtil authenticationControllerHelperUtil;
	
	private QuestionService questionService;

	@Autowired
	public QuestionRestController(AuthenticationControllerHelperUtil authenticationControllerHelperUtil,
			QuestionService questionService) {
		super();
		this.authenticationControllerHelperUtil = authenticationControllerHelperUtil;
		this.questionService = questionService;
	}
	
	@PostMapping("/post_question/{productId}")
	public ResponseEntity<?> postQuestion(@RequestBody Question question,
			@PathVariable(name = "productId") Integer productId,
			HttpServletRequest request) throws ProductNotFoundException, IOException {

		LOGGER.info("QuestionRestController | postQuestion is called");
		
		LOGGER.info("QuestionRestController | postQuestion | question : " + question.toString());
		LOGGER.info("QuestionRestController | postQuestion | productId : " + productId);
		
		Customer customerUser = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		LOGGER.info("QuestionRestController | postQuestion | customerUser : " + customerUser.getFullName());
		
		if (customerUser == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		questionService.saveNewQuestion(question, customerUser, productId);

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/*@PostMapping("/post_question/{productId}")
	public ResponseEntity<?> postQuestion(@RequestBody Map<String,String> data,
			@PathVariable(name = "productId") Integer productId,
			HttpServletRequest request) throws ProductNotFoundException, IOException {
		
		LOGGER.info("QuestionRestController | postQuestion is called");
		
		LOGGER.info("QuestionRestController | postQuestion | productId : " + productId);
		
		String questionContent = data.get("questionContent");
		LOGGER.info("QuestionRestController | postQuestion | questionContent : " + questionContent);
		
		Question question = new Question();
		question.setQuestionContent(questionContent);
		
		Customer customerUser = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		LOGGER.info("QuestionRestController | postQuestion | customerUser : " + customerUser.getFullName());
		
		if (customerUser == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		questionService.saveNewQuestion(question, customerUser, productId);

		return new ResponseEntity<>(HttpStatus.OK);
	}*/
	
}
