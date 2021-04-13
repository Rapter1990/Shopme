package com.shopme.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.error.CategoryNotFoundException;
import com.shopme.admin.service.CategoryService;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.Category;

@Controller
public class CategoryController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/categories")
	public String listAll(@Param("sortDir") String sortDir, Model model) {
		
		LOGGER.info("CategoryController | listAll is started");
		
		if (sortDir ==  null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		
		List<Category> listCategories = categoryService.listAll(sortDir);

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("reverseSortDir", reverseSortDir);
		
		LOGGER.info("CategoryController | listAll | listCategories : " + listCategories.toString());
		LOGGER.info("CategoryController | listAll | reverseSortDir : " + reverseSortDir);

		return "categories/categories";
	}
	
	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		
		LOGGER.info("CategoryController | newCategory is started");
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();

		model.addAttribute("category", new Category());
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Create New Category");
		
		LOGGER.info("CategoryController | newCategory | listCategories : " + listCategories.toString());

		return "categories/category_form";
		
	}
	
	@PostMapping("/categories/save")
	public String saveCategory(Category category, 
			@RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException {
		
		LOGGER.info("CategoryController | saveCategory is started");
		
		LOGGER.info("CategoryController | saveCategory | multipartFile.isEmpty() : " + multipartFile.isEmpty());
		
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			
			LOGGER.info("CategoryController | saveCategory | fileName : " + fileName);
			
			category.setImage(fileName);

			Category savedCategory = categoryService.save(category);
			String uploadDir = "../category-images/" + savedCategory.getId();
			
			LOGGER.info("CategoryController | saveCategory | savedCategory : " + savedCategory.toString());
			LOGGER.info("CategoryController | saveCategory | uploadDir : " + uploadDir);
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			categoryService.save(category);
		}
				
		ra.addFlashAttribute("messageSuccess", "The category has been saved successfully.");
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes ra) {
		
		LOGGER.info("CategoryController | editCategory is started");
		
		try {
			Category category = categoryService.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			
			LOGGER.info("CategoryController | editCategory | category : " + category.toString());
			LOGGER.info("CategoryController | editCategory | listCategories : " + listCategories.toString());
			

			model.addAttribute("category", category);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");

			return "categories/category_form";			
			
		} catch (CategoryNotFoundException ex) {
			
			LOGGER.info("CategoryController | editCategory | messageError : " + ex.getMessage());
			ra.addFlashAttribute("messageError", ex.getMessage());
			return "redirect:/categories";
		}
	}
	
	@GetMapping("/categories/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		
		LOGGER.info("CategoryController | exportToCSV is started");
		
	}
	
	@GetMapping("/categories/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		
		LOGGER.info("CategoryController | exportToExcel is started");
		
	}
	
	@GetMapping("/categories/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		
		LOGGER.info("CategoryController | exportToPDF is started");
		
	}
}
