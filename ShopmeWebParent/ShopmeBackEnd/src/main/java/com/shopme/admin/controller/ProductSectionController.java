package com.shopme.admin.controller;

import javax.servlet.http.HttpServletRequest;

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
import com.shopme.admin.util.SectionUtil;
import com.shopme.common.entity.section.Section;
import com.shopme.common.entity.section.SectionType;
import com.shopme.common.exception.SectionNotFoundException;

@Controller
public class ProductSectionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSectionController.class);
	
	@Autowired
	private SectionService service;

	@GetMapping("/sections/new/product")
	public String showForm(Model model) {
		
		LOGGER.info("ProductSectionController | showForm is called");
		
		Section section = new Section(true, SectionType.PRODUCT);

		LOGGER.info("GeneralSectionController | listAllSections | section : " + section.toString());
		LOGGER.info("GeneralSectionController | listAllSections | pageTitle : " + "Add Product Section");
		
		model.addAttribute("section", section);
		model.addAttribute("pageTitle", "Add Product Section");

		return "sections/product_section_form";
	}


	@PostMapping("/sections/save/product")
	public String saveSection(Section section, HttpServletRequest request, RedirectAttributes ra) {
		SectionUtil.addProductsToSection(section, request);
		service.saveSection(section);
		LOGGER.info("ProductSectionController | saveSection | messageSuccess : " + "The section of type Product has been saved successfully.");
		ra.addFlashAttribute("messageSuccess", "The section of type Product has been saved successfully.");
		return "redirect:/sections";
	}
	
	@GetMapping("/sections/edit/Product/{id}")
	public String editSection(@PathVariable(name = "id") Integer id, RedirectAttributes ra,
			Model model) {
		
		LOGGER.info("ProductSectionController | editSection is called");
		
		try {
			Section section = service.getSection(id);
			
			LOGGER.info("ProductSectionController | editSection | section : " + section.toString());
			LOGGER.info("ProductSectionController | editSection | pageTitle : " + "Edit Product Section (ID: " + id + ")");

			model.addAttribute("section", section);
			model.addAttribute("pageTitle", "Edit Product Section (ID: " + id + ")");

			return "sections/product_section_form";

		} catch (SectionNotFoundException ex) {
			LOGGER.info("ProductSectionController | editSection | messageError : " + ex.getMessage());
			ra.addFlashAttribute("messageError", ex.getMessage());
			return "redirect:/sections";		
		}

	}
}
