package com.shopme.admin.controller;

import java.util.List;

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

import com.shopme.admin.service.BrandService;
import com.shopme.admin.service.SectionService;
import com.shopme.admin.util.SectionUtil;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.section.Section;
import com.shopme.common.entity.section.SectionType;
import com.shopme.common.exception.SectionNotFoundException;

@Controller
public class BrandSectionController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BrandSectionController.class);

	private SectionService sectionService;
	
	private BrandService brandService;

	@Autowired
	public BrandSectionController(SectionService sectionService, BrandService brandService) {
		super();
		this.sectionService = sectionService;
		this.brandService = brandService;
	}
	
	@GetMapping("/sections/new/brand")
	public String showForm(Model model) {
		
		LOGGER.info("BrandSectionController | showForm is called");
		
		Section section = new Section(true, SectionType.BRAND);
		List<Brand> listBrands = brandService.listAll();
		
		LOGGER.info("BrandSectionController | showForm | listBrands size : " + listBrands.size());
		LOGGER.info("BrandSectionController | showForm | section : " + section.toString());
		LOGGER.info("BrandSectionController | showForm | pageTitle : " + "Add Brand Section");

		model.addAttribute("listBrands", listBrands);
		model.addAttribute("section", section);
		model.addAttribute("pageTitle", "Add Brand Section");

		return "sections/brand_section_form";
	}		

	@PostMapping("/sections/save/brand")
	public String saveSection(Section section, HttpServletRequest request, RedirectAttributes ra) {
		
		LOGGER.info("BrandSectionController | saveSection is called");
		
		SectionUtil.addBrandsToSection(section, request);
		sectionService.saveSection(section);

		LOGGER.info("BrandSectionController | saveSection | messageSuccess : " + "The section of type Brand has been saved successfully.");
		
		
		ra.addFlashAttribute("message", "The section of type Brand has been saved successfully.");
		return "redirect:/sections";
	}
	
	@GetMapping("/sections/edit/Brand/{id}")
	public String editSection(@PathVariable(name = "id") Integer id, RedirectAttributes ra,
			Model model) {
		
		LOGGER.info("BrandSectionController | editSection is called");
		
		try {
			Section section = sectionService.getSection(id);
			List<Brand> listBrands = brandService.listAll();
			
			LOGGER.info("BrandSectionController | viewArticle | listBrands size : " + listBrands.size());
			LOGGER.info("BrandSectionController | viewArticle | section : " + section.toString());
			LOGGER.info("BrandSectionController | viewArticle | pageTitle : " + "Edit Brand Section (ID: " + id + ")");

			model.addAttribute("listBrands", listBrands);
			model.addAttribute("section", section);
			model.addAttribute("pageTitle", "Edit Brand Section (ID: " + id + ")");

			return "sections/brand_section_form";

		} catch (SectionNotFoundException ex) {
			LOGGER.info("BrandSectionController | editSection | messageError : " + ex.getMessage());
			ra.addFlashAttribute("messageError", ex.getMessage());
			return "redirect:/sections";		
		}

	}
}
