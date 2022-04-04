package com.shopme.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.service.QuestionService;

@Controller
public class QuestionController {

	private String defaultRedirectURL = "redirect:/questions/page/1?sortField=askTime&sortDir=desc";
	
	@Autowired 
	private QuestionService service;
	
	@GetMapping("/questions")
	public String listFirstPage(Model model) {
		return defaultRedirectURL;
	}
	
	@GetMapping("/questions/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listQuestions", moduleURL = "/questions") PagingAndSortingHelper helper,
						@PathVariable(name = "pageNum") int pageNum) {

		service.listByPage(pageNum, helper);

		return "questions/questions";		
	}
}
