package com.shopme.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.service.ArticleService;
import com.shopme.common.entity.User;
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
	
	@GetMapping("/articles/new")
	public String newArticle(Model model) {
		
		LOGGER.info("ArticleController | newArticle is called");
		
		model.addAttribute("article", new Article());
		model.addAttribute("pageTitle", "Create New Article");
		
		LOGGER.info("ArticleController | newArticle | pageTitle : " + "Create New Article");

		return "articles/article_form";
	}
	
	@PostMapping("/articles/save")
	public String saveArticle(Article article, RedirectAttributes ra, 
			@AuthenticationPrincipal ShopmeUserDetails userDetails) {

		LOGGER.info("ArticleController | saveArticle is called");
		
		User authenticatedUser = userDetails.getUser();
		
		LOGGER.info("ArticleController | saveArticle | authenticatedUser : " + authenticatedUser.getFullName());
		
		service.save(article, authenticatedUser);
		
		LOGGER.info("ArticleController | saveArticle | message : " + "The article has been saved successfully.");

		ra.addFlashAttribute("messageSuccess", "The article has been saved successfully.");

		return defaultRedirectURL;
	}
	
	@GetMapping("/articles/edit/{id}")
	public String editArticle(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes ra) {
		
		LOGGER.info("ArticleController | editArticle is called");
		
		try {
			Article article = service.get(id);
			
			LOGGER.info("ArticleController | editArticle | article content: " + article.getContent());
			model.addAttribute("article", article);
			
			LOGGER.info("ArticleController | editArticle | pageTitle: " + "Edit Article (ID: " + id + ")");
			model.addAttribute("pageTitle", "Edit Article (ID: " + id + ")");

			return "articles/article_form"; 

		} catch (ArticleNotFoundException ex) {
			LOGGER.info("ArticleController | editArticle | messageError: " + ex.getMessage());
			ra.addFlashAttribute("messageError", ex.getMessage());

			return defaultRedirectURL;
		}		
	}
	
	
	@GetMapping("/articles/delete/{id}")
	public String deleteArticle(@PathVariable(name = "id") Integer id, RedirectAttributes ra) {
		
		LOGGER.info("ArticleController | deleteArticle is called");
		LOGGER.info("ArticleController | deleteArticle | id : " + id);
		
		try {
			service.delete(id);
			
			LOGGER.info("ArticleController | editArticle | messageSuccess: " + "The article ID " + id + " has been deleted.");
			ra.addFlashAttribute("messageSuccess", "The article ID " + id + " has been deleted.");
		} catch (ArticleNotFoundException ex) {
			LOGGER.info("ArticleController | deleteArticle | messageError: " + ex.getMessage());
			ra.addFlashAttribute("messageError", ex.getMessage());
		}

		return defaultRedirectURL;
	}
	
	@GetMapping("/articles/{id}/enabled/{publishStatus}")
	public String publishArticle(@PathVariable("id") Integer id, 
			@PathVariable("publishStatus") String publishStatus, RedirectAttributes ra) {
		
		LOGGER.info("ArticleController | publishArticle is called");
		LOGGER.info("ArticleController | publishArticle | id : " + id);
		LOGGER.info("ArticleController | publishArticle | publishStatus : " + publishStatus);
		
		try {
			boolean published = Boolean.parseBoolean(publishStatus);	
			
			LOGGER.info("ArticleController | publishArticle | published : " + published);
			
			service.updatePublishStatus(id, published);		

			String publishResult = published ? "published." : "unpublished.";
			
			LOGGER.info("ArticleController | publishArticle | publishResult : " + publishResult);
			
			LOGGER.info("ArticleController | publishArticle | messageSuccess : " + "The article ID " + id + " has been " + publishResult);
			ra.addFlashAttribute("messageSuccess", "The article ID " + id + " has been " + publishResult);
				
		} catch (ArticleNotFoundException ex) {
			LOGGER.info("ArticleController | publishArticle | messageError: " + ex.getMessage());
			ra.addFlashAttribute("messageError", ex.getMessage());
		}

		return defaultRedirectURL;
	}
}
