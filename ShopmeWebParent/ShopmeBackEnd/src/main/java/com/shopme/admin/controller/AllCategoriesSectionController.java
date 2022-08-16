package com.shopme.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.common.section.Section;
import com.shopme.common.section.SectionType;

@Controller
public class AllCategoriesSectionController {

	@GetMapping("/sections/new/all_categories")
	public String showForm(Model model) {
		Section section = new Section(true, SectionType.ALL_CATEGORIES);

		model.addAttribute("section", section);
		model.addAttribute("pageTitle", "Add All Categories Section");

		return "sections/all_categories_section_form";
	}
}
