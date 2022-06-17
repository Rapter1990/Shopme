package com.shopme.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.service.ArticleService;
import com.shopme.admin.service.MenuService;
import com.shopme.common.entity.article.Article;
import com.shopme.common.entity.menu.Menu;
import com.shopme.common.exception.MenuItemNotFoundException;

@Controller
public class MenuController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);
	
	private final String defaultRedirectURL = "redirect:/menus";
	
	private MenuService menuService;
	
	private ArticleService articleService;
	
	@Autowired
	public MenuController(MenuService menuService, ArticleService articleService) {
		super();
		this.menuService = menuService;
		this.articleService = articleService;
	}

	@GetMapping("/menus")
	public String listAll(Model model) {
		
		LOGGER.info("MenuController | listAll is called");
		List<Menu> listMenuItems = menuService.listAll();
		
		LOGGER.info("MenuController | listAll | listMenuItems size :" + listMenuItems.size());
		model.addAttribute("listMenuItems", listMenuItems);

		return "menus/menu_items";
	}
	
	@GetMapping("menus/new")
	public String newMenu(Model model) {
		
		LOGGER.info("MenuController | newMenu is called");
		
		List<Article> listArticles = articleService.listArticlesForMenu();
		
		LOGGER.info("MenuController | newMenu | listArticles : " + listArticles.toString());

		model.addAttribute("menu", new Menu());
		model.addAttribute("listArticles", listArticles);
		model.addAttribute("pageTitle", "Create New Menu Item");
		
		LOGGER.info("MenuController | newMenu | pageTitle : " + "Create New Menu Item");

		return "menus/menu_form";
	}

	@PostMapping("/menus/save")
	public String saveMenu(Menu menu, RedirectAttributes ra) {
		
		LOGGER.info("MenuController | saveMenu is called");
		
		menuService.save(menu);
		
		ra.addFlashAttribute("messageSuccess", "The menu item has been saved successfully.");
		
		LOGGER.info("MenuController | saveMenu | messageSuccess : " + "The menu item has been saved successfully.");

		return defaultRedirectURL;
	}
	
	@GetMapping("/menus/edit/{id}")
	public String editMenu(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra) {
		
		LOGGER.info("MenuController | editMenu is called");
		LOGGER.info("MenuController | editMenu | id : " + id);
		
		try {
			Menu menu = menuService.get(id);
			
			LOGGER.info("MenuController | editMenu | id : " + id);
			
			List<Article> listArticles = articleService.listArticlesForMenu();
			
			LOGGER.info("MenuController | editMenu | listArticles : " + listArticles.toString());
			
			LOGGER.info("MenuController | editMenu | menu : " + menu.toString());

			model.addAttribute("menu", menu);
			model.addAttribute("listArticles", listArticles);
			model.addAttribute("pageTitle", "Edit Menu Item (ID: " + id + ")");

			return "menus/menu_form";
		} catch (MenuItemNotFoundException ex) {
			ra.addFlashAttribute("messageError", ex.getMessage());
			LOGGER.info("MenuController | editMenu | messageError : " + ex.getMessage());
			return defaultRedirectURL;
		}
	}
	
	@GetMapping("/menus/{id}/enabled/{enabledStatus}")
	public String updateMenuEnabledStatus(@PathVariable("id") Integer id, 
			@PathVariable("enabledStatus") String enabledStatus, RedirectAttributes ra) {
		
		LOGGER.info("MenuController | updateMenuEnabledStatus is called");
		LOGGER.info("MenuController | updateMenuEnabledStatus | id : " + id);
		LOGGER.info("MenuController | updateMenuEnabledStatus | enabledStatus : " + enabledStatus);
		
		try {
			boolean enabled = Boolean.parseBoolean(enabledStatus);
			
			LOGGER.info("MenuController | updateMenuEnabledStatus | enabled : " + enabled);
			
			menuService.updateEnabledStatus(id, enabled);		

			String updateResult = enabled ? "enabled." : "disabled.";
			
			LOGGER.info("MenuController | updateMenuEnabledStatus | updateResult : " + updateResult);
			
			ra.addFlashAttribute("messageSuccess", "The menu item ID " + id + " has been " + updateResult);
			
			LOGGER.info("MenuController | updateMenuEnabledStatus | messageSuccess : " + "The menu item ID " + id + " has been " + updateResult);
			
		} catch (MenuItemNotFoundException ex) {
			LOGGER.info("MenuController | updateMenuEnabledStatus | messageError : " + ex.getMessage());
			
			ra.addFlashAttribute("messageError", ex.getMessage());
		}

		return defaultRedirectURL;
	}
}
