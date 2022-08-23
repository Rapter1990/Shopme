package com.shopme.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.service.SectionService;
import com.shopme.common.entity.section.Section;
import com.shopme.common.entity.section.SectionType;

@Controller
public class AllCategoriesSectionController {

	@Autowired
	private SectionService service;
	
	@GetMapping("/sections/new/all_categories")
	public String showForm(Model model) {
		Section section = new Section(true, SectionType.ALL_CATEGORIES);

		model.addAttribute("section", section);
		model.addAttribute("pageTitle", "Add All Categories Section");

		return "sections/all_categories_section_form";
	}
	
	@PostMapping("/sections/save/all_categories")
	public String saveSection(Section section, RedirectAttributes ra) {
		service.saveSection(section);
		ra.addFlashAttribute("message", "The section of type All Categories has been saved successfully.");

		return "redirect:/sections";
	}
	
}
