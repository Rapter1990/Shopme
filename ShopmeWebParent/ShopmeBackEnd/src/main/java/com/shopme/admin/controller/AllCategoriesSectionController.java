package com.shopme.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.service.SectionService;
import com.shopme.common.entity.section.Section;
import com.shopme.common.entity.section.SectionType;
import com.shopme.common.exception.SectionNotFoundException;

@Controller
public class AllCategoriesSectionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AllCategoriesSectionController.class);
	
	@Autowired
	private SectionService service;
	
	@GetMapping("/sections/new/all_categories")
	public String showForm(Model model) {
		
		LOGGER.info("AllCategoriesSectionController | showForm is called");
		
		Section section = new Section(true, SectionType.ALL_CATEGORIES);
		
		LOGGER.info("AllCategoriesSectionController | showForm | section : " + section.toString());

		model.addAttribute("section", section);
		model.addAttribute("pageTitle", "Add All Categories Section");

		return "sections/all_categories_section_form";
	}
	
	@PostMapping("/sections/save/all_categories")
	public String saveSection(Section section, RedirectAttributes ra) {
		
		LOGGER.info("AllCategoriesSectionController | saveSection is called");
		
		service.saveSection(section);
		
		LOGGER.info("AllCategoriesSectionController | saveSection | message : The section of type All Categories has been saved successfully.");
		
		ra.addFlashAttribute("message", "The section of type All Categories has been saved successfully.");

		return "redirect:/sections";
	}
	
	@GetMapping("/sections/edit/All_Categories/{id}")
	public String editSection(@PathVariable(name = "id") Integer id, RedirectAttributes ra,
			Model model) {
		
		LOGGER.info("AllCategoriesSectionController | editSection is called");
		
		
		try {
			Section section = service.getSection(id);
			
			LOGGER.info("AllCategoriesSectionController | editSection | section : " + section.toString());

			model.addAttribute("section", section);
			
			LOGGER.info("AllCategoriesSectionController | editSection | pageTitle : " + "Edit All Categories Section (ID: " + id + ")");
			
			model.addAttribute("pageTitle", "Edit All Categories Section (ID: " + id + ")");

			return "sections/all_categories_section_form";

		} catch (SectionNotFoundException ex) {
			
			LOGGER.info("AllCategoriesSectionController | editSection | messageError : " + ex.getMessage());
			
			ra.addFlashAttribute("messageError", ex.getMessage());
			return "redirect:/sections";		
		}

	}
}
