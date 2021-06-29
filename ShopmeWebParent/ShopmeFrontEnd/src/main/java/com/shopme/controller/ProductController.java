package com.shopme.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.service.CategoryService;
import com.shopme.service.ProductService;

@Controller
public class ProductController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired 
	private CategoryService categoryService;
	
	@Autowired 
	private ProductService productService;
	
	@GetMapping("/c/{category_alias}")
	public String viewCategoryFirstPage(@PathVariable("category_alias") String alias,
			Model model) {
		
		LOGGER.info("AccountController | viewCategoryFirstPage is called");
		
		return viewCategoryByPage(alias, 1, model);
	}
	
	@GetMapping("/c/{category_alias}/page/{pageNum}")
	public String viewCategoryByPage(@PathVariable("category_alias") String alias,
			@PathVariable("pageNum") int pageNum,
			Model model) {
		
		LOGGER.info("AccountController | viewCategoryByPage is called");
		
		Category category = categoryService.getCategory(alias);
		
		LOGGER.info("AccountController | viewCategoryByPage | category : " + category.toString());
		
		if (category == null) {
			LOGGER.info("AccountController | viewCategoryByPage | category == null");
			return "error/404";
		}

		List<Category> listCategoryParents = categoryService.getCategoryParents(category);
		
		LOGGER.info("AccountController | viewCategoryByPage | listCategoryParents : " + listCategoryParents.toString());

		Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());
		
		List<Product> listProducts = pageProducts.getContent();
		
		LOGGER.info("AccountController | viewCategoryByPage | listProducts : " + listProducts.toString());

		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		
		LOGGER.info("AccountController | viewCategoryByPage | startCount : " + startCount);
		LOGGER.info("AccountController | viewCategoryByPage | endCount : " + endCount);
		
		LOGGER.info("AccountController | viewCategoryByPage | endCount > pageProducts.getTotalElements()");
		if (endCount > pageProducts.getTotalElements()) {
			LOGGER.info("AccountController | viewCategoryByPage | endCount > pageProducts.getTotalElements() | endCount : " + endCount);
			LOGGER.info("AccountController | viewCategoryByPage | endCount > pageProducts.getTotalElements() | pageProducts.getTotalElements() : " + pageProducts.getTotalElements());
			LOGGER.info("AccountController | viewCategoryByPage | endCount > pageProducts.getTotalElements()");
			endCount = pageProducts.getTotalElements();
		}

		LOGGER.info("AccountController | viewCategoryByPage | currentPage : " + pageNum);
		LOGGER.info("AccountController | viewCategoryByPage | totalPages : " + pageProducts.getTotalPages());
		LOGGER.info("AccountController | viewCategoryByPage | startCount : " + startCount);
		LOGGER.info("AccountController | viewCategoryByPage | endCount : " + endCount);
		LOGGER.info("AccountController | viewCategoryByPage | totalItems : " + pageProducts.getTotalElements());
		LOGGER.info("AccountController | viewCategoryByPage | pageTitle : " + category.getName());
		LOGGER.info("AccountController | viewCategoryByPage | listCategoryParents : " + listCategoryParents);
		LOGGER.info("AccountController | viewCategoryByPage | listProducts : " + listProducts);
		LOGGER.info("AccountController | viewCategoryByPage | category : " + category.toString());
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", pageProducts.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageProducts.getTotalElements());
		model.addAttribute("pageTitle", category.getName());
		model.addAttribute("listCategoryParents", listCategoryParents);
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("category", category);

		return "products_by_category";
	}
}
