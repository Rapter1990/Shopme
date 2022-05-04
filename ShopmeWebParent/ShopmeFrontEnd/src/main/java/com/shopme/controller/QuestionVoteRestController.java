package com.shopme.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.VoteResultDTO;
import com.shopme.common.entity.VoteType;
import com.shopme.service.QuestionVoteService;
import com.shopme.util.AuthenticationControllerHelperUtil;

@RestController
public class QuestionVoteRestController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionVoteRestController.class);

	private AuthenticationControllerHelperUtil authenticationControllerHelperUtil;

	private QuestionVoteService service;
	
	public QuestionVoteRestController(AuthenticationControllerHelperUtil authenticationControllerHelperUtil,
			QuestionVoteService service) {
		super();
		this.authenticationControllerHelperUtil = authenticationControllerHelperUtil;
		this.service = service;
	}

	@PostMapping("/vote_question/{id}/{type}")
	public VoteResultDTO voteQuestion(@PathVariable(name = "id") Integer questionId,
			@PathVariable(name = "type") String type, HttpServletRequest request) {

		LOGGER.info("QuestionController | voteQuestion is called");
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		LOGGER.info("QuestionController | voteQuestion | customer : " + customer.getFullName());
		
		if (customer == null) {
			return VoteResultDTO.fail("You must login to vote the question.");
		}

		VoteType voteType = VoteType.valueOf(type.toUpperCase());

		return service.doVote(questionId, customer, voteType);		
	}

}
