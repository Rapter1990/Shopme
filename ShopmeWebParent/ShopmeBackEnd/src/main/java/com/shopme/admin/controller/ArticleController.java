package com.shopme.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.service.ArticleService;

@Controller
public class ArticleController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);
	
	private String defaultRedirectURL = "redirect:/articles/page/1?sortField=title&sortDir=asc";

	@Autowired 
	private ArticleService service;

	@GetMapping("/articles")
	public String listFirstPage(Model model) {
		
		LOGGER.info("ArticleController | listFirstPage is called");
		
		return defaultRedirectURL;
	}

	@GetMapping("/articles/page/{pageNum}")
	public String listByPage(@PagingAndSortingParam(moduleURL = "/articles", listName = "listArticles") PagingAndSortingHelper helper, 
						@PathVariable(name = "pageNum") int pageNum) {
		
		LOGGER.info("ArticleController | listByPage is called");
		
		service.listByPage(pageNum, helper);
		return "articles/articles";
	}
}
