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

import com.shopme.admin.service.ArticleService;
import com.shopme.admin.service.SectionService;
import com.shopme.admin.util.SectionUtil;
import com.shopme.common.entity.article.Article;
import com.shopme.common.entity.section.Section;
import com.shopme.common.entity.section.SectionType;
import com.shopme.common.exception.SectionNotFoundException;

@Controller
public class ArticleSectionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleSectionController.class);
	
	private SectionService sectionService;

	private ArticleService articleService;
	
	@Autowired
	public ArticleSectionController(SectionService sectionService, ArticleService articleService) {
		super();
		this.sectionService = sectionService;
		this.articleService = articleService;
	}

	@GetMapping("/sections/new/article")
	public String showForm(Model model) {
		
		LOGGER.info("ArticleSectionController | showForm is called");
		
		Section section = new Section(true, SectionType.ARTICLE);
		List<Article> listArticles = articleService.listAll();

		LOGGER.info("ArticleSectionController | showForm | listArticles size : " + listArticles.size());
		LOGGER.info("ArticleSectionController | showForm | section : " + section.toString());
		LOGGER.info("ArticleSectionController | showForm | pageTitle : " + "Add Article Section");
		
		model.addAttribute("listArticles", listArticles);
		model.addAttribute("section", section);
		model.addAttribute("pageTitle", "Add Article Section");

		return "sections/article_section_form";
	}
	
	@PostMapping("/sections/save/article")
	public String saveSection(Section section, HttpServletRequest request, RedirectAttributes ra) {
		
		LOGGER.info("ArticleSectionController | saveSection is called");
		
		SectionUtil.addArticlesToSection(section, request);
		sectionService.saveSection(section);
		
		LOGGER.info("ArticleSectionController | saveSection | messageSuccess : " + "The section of type Article has been saved successfully.");
		
		ra.addFlashAttribute("messageSuccess", "The section of type Article has been saved successfully.");
		return "redirect:/sections";
	}		
		

	@GetMapping("/sections/edit/Article/{id}")
	public String editSection(@PathVariable(name = "id") Integer id, RedirectAttributes ra,
			Model model) {
		
		LOGGER.info("ArticleSectionController | editSection is called");
		
		try {
			Section section = sectionService.getSection(id);
			List<Article> listArticles = articleService.listAll();
			
			LOGGER.info("ArticleSectionController | editSection | listArticles size : " + listArticles.size());
			LOGGER.info("ArticleSectionController | editSection | section : " + section.toString());
			LOGGER.info("ArticleSectionController | editSection | pageTitle : " + "Edit Article Section (ID: " + id + ")");
			

			model.addAttribute("listArticles", listArticles);
			model.addAttribute("section", section);
			model.addAttribute("pageTitle", "Edit Article Section (ID: " + id + ")");

			return "sections/article_section_form";

		} catch (SectionNotFoundException ex) {
			LOGGER.info("ArticleSectionController | editSection | messageError : " + ex.getMessage());
			ra.addFlashAttribute("messageError", ex.getMessage());
			return "redirect:/sections";		
		}

	}
}
