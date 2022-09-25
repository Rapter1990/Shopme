package com.shopme.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.common.entity.article.Article;
import com.shopme.repository.ArticleRepository;

@Controller
public class ArticleController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);
	
	@Autowired
	private ArticleRepository repo;

	@GetMapping("/a/{alias}")
	public String viewArticle(@PathVariable("alias") String alias, Model model) {
		
		LOGGER.info("ArticleController | viewArticle is called");
		
		Article article = repo.findByAlias(alias);
		
		LOGGER.info("ArticleController | viewArticle | article : " + article.toString());

		if (article == null ) {
			LOGGER.info("ArticleController | viewArticle | return error/404");
			return "error/404";
		}

		model.addAttribute("article", article);
		return "article";
	}
}
