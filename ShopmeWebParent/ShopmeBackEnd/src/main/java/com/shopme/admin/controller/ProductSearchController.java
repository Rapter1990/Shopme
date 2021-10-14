package com.shopme.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.service.ProductService;

@Controller
public class ProductSearchController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSearchController.class);

	@Autowired 
	private ProductService service;

	@GetMapping("/orders/search_product")
	public String showSearchProductPage() {
		
		LOGGER.info("ProductSearchController | showSearchProductPage is started");
		
		return "orders/search_product";
	}

	@PostMapping("/orders/search_product")
	public String searchProducts(String keyword) {
		
		LOGGER.info("ProductSearchController | searchProducts is started");
		
		LOGGER.info("ProductSearchController | searchProducts | keyword : " + keyword);
		
		return "redirect:/orders/search_product/page/1?sortField=name&sortDir=asc&keyword=" + keyword;
	}

	@GetMapping("/orders/search_product/page/{pageNum}")
	public String searchProductsByPage(@PagingAndSortingParam(listName = "listProducts", 
			moduleURL = "/orders/search_product") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum) {
		
		LOGGER.info("ProductSearchController | searchProductsByPage is started");
		
		LOGGER.info("ProductSearchController | searchProductsByPage | helper : " + helper);
		
		LOGGER.info("ProductSearchController | searchProductsByPage | pageNum : " + pageNum);
		
		service.searchProducts(pageNum, helper);
		
		return "orders/search_product";
	}
}
