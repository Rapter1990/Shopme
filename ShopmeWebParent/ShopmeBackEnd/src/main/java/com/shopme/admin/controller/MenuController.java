package com.shopme.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.admin.service.MenuService;
import com.shopme.common.entity.menu.Menu;

@Controller
public class MenuController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);
	
	@Autowired 
	private MenuService menuService;
	
	@GetMapping("/menus")
	public String listAll(Model model) {
		
		LOGGER.info("MenuController | listAll is called");
		List<Menu> listMenuItems = menuService.listAll();
		
		LOGGER.info("MenuController | listAll | listMenuItems size :" + listMenuItems.size());
		model.addAttribute("listMenuItems", listMenuItems);

		return "menus/menu_items";
	}	
}
