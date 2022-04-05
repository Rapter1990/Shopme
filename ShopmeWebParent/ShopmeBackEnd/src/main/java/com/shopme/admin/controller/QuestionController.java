package com.shopme.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.service.QuestionService;
import com.shopme.common.entity.question.Question;
import com.shopme.common.exception.QuestionNotFoundException;

@Controller
public class QuestionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);
	
	private String defaultRedirectURL = "redirect:/questions/page/1?sortField=askTime&sortDir=desc";
	
	@Autowired 
	private QuestionService service;
	
	@GetMapping("/questions")
	public String listFirstPage(Model model) {
		
		LOGGER.info("QuestionController | listFirstPage is called");
		
		return defaultRedirectURL;
	}
	
	@GetMapping("/questions/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listQuestions", moduleURL = "/questions") PagingAndSortingHelper helper,
						@PathVariable(name = "pageNum") int pageNum) {

		LOGGER.info("QuestionController | listByPage is called");
		LOGGER.info("QuestionController | listByPage | pageNum : " + pageNum);
		
		service.listByPage(pageNum, helper);

		return "questions/questions";		
	}
	
	@GetMapping("/questions/detail/{id}")
	public String viewQuestion(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		
		LOGGER.info("QuestionController | viewQuestion is called");
		LOGGER.info("QuestionController | viewQuestion | id : " + id);
		
		try {
			Question question = service.getQuestionById(id);
			
			LOGGER.info("QuestionController | viewQuestion | question : " + question.toString());
			
			model.addAttribute("question", question);

			return "questions/question_detail_modal";
			
		} catch (QuestionNotFoundException ex) {
			LOGGER.info("QuestionController | viewQuestion | message : " + ex.getMessage());
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;	
		}
	}
}
