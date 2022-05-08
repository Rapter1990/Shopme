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
import com.shopme.admin.service.ArticleService;
import com.shopme.common.entity.article.Article;
import com.shopme.common.exception.ArticleNotFoundException;

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
	
	@GetMapping("/articles/detail/{id}")
	public String viewArticle(@PathVariable(name = "id") Integer id, RedirectAttributes ra,  Model model) {
		
		LOGGER.info("ArticleController | viewArticle is called");
		
		try {
			Article article = service.get(id);
			
			LOGGER.info("ArticleController | viewArticle | article title : " + article.getTitle());
			
			model.addAttribute("article", article);

			return "articles/article_detail_modal";

		} catch (ArticleNotFoundException ex) {
			
			LOGGER.info("ArticleController | viewArticle | messageSuccess : " + "Could not find any article with ID " + id);
			
			ra.addFlashAttribute("messageSuccess", "Could not find any article with ID " + id);
			return defaultRedirectURL;
		}		
	}
}
