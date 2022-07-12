package com.shopme.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.common.entity.article.Article;
import com.shopme.common.exception.MenuItemNotFoundException;
import com.shopme.service.MenuService;

@Controller
public class MenuController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);
	
	@Autowired 
	private MenuService service;

	@GetMapping("/m/{alias}")
	public String viewMenu(@PathVariable(name = "alias") String alias, Model model) {
		
		LOGGER.info("MenuController | viewMenu is called");
		
		try {
			Article article = service.getArticleBoundToMenu(alias);
			
			LOGGER.info("MenuController | viewMenu | pageTitle : " + article.getTitle());
			LOGGER.info("MenuController | viewMenu | article : " + article.toString());
			
			model.addAttribute("pageTitle", article.getTitle());
			model.addAttribute("article", article);

		} catch (MenuItemNotFoundException ex) {
			LOGGER.info("MenuController | viewMenu | MenuItemNotFoundException | error/404");
			return "error/404";
		}

		return "article";
	}	
}
