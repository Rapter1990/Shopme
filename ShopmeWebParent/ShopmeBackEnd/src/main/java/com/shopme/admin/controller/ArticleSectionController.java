package com.shopme.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.admin.service.ArticleService;
import com.shopme.common.entity.article.Article;
import com.shopme.common.entity.section.Section;
import com.shopme.common.entity.section.SectionType;

@Controller
public class ArticleSectionController {

	@Autowired
	private ArticleService articleService;
	
	@GetMapping("/sections/new/article")
	public String showForm(Model model) {
		Section section = new Section(true, SectionType.ARTICLE);
		List<Article> listArticles = articleService.listAll();

		model.addAttribute("listArticles", listArticles);
		model.addAttribute("section", section);
		model.addAttribute("pageTitle", "Add Article Section");

		return "sections/article_section_form";
	}
}
