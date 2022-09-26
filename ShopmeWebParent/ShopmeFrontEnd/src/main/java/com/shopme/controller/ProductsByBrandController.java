package com.shopme.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.product.Product;
import com.shopme.repository.BrandRepository;
import com.shopme.service.ProductService;

@Controller
public class ProductsByBrandController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductsByBrandController.class);

	private BrandRepository brandRepo;
	
	private ProductService productService;
	
	
	@Autowired
	public ProductsByBrandController(BrandRepository brandRepo, ProductService productService) {
		super();
		this.brandRepo = brandRepo;
		this.productService = productService;
	}

	@GetMapping("/brand/{brand_id}")
	public String listProductsByBrand(@PathVariable(name = "brand_id") Integer brandId, Model model) {
		
		LOGGER.info("ProductsByBrandController | listProductsByBrand is called");
		
		return listProductsByBrandByPage(brandId, 1, model);
	}

	@GetMapping("/brand/{brand_id}/page/{pageNum}")
	public String listProductsByBrandByPage(@PathVariable(name = "brand_id") Integer brandId,
			@PathVariable(name = "pageNum") int pageNum,
			Model model) {
		
		LOGGER.info("ProductsByBrandController | listProductsByBrandByPage is called");
		
		LOGGER.info("ProductsByBrandController | listProductsByBrandByPage | brandId : " + brandId);
		LOGGER.info("ProductsByBrandController | listProductsByBrandByPage | pageNum : " + pageNum);
		
		Optional<Brand> brandById = brandRepo.findById(brandId);
		
		if (!brandById.isPresent()) {
			
			LOGGER.info("ProductsByBrandController | listProductsByBrandByPage | error/404");
			return "error/404";
		}

		
		Brand brand = brandById.get();
		
		LOGGER.info("ProductsByBrandController | listProductsByBrandByPage | brand : " + brand.toString());

		Page<Product> pageProducts = productService.listByBrand(pageNum, brand.getId());
		List<Product> listProducts = pageProducts.getContent();
		
		LOGGER.info("ProductsByBrandController | listProductsByBrandByPage | listProducts size : " + listProducts.size());

		LOGGER.info("ProductsByBrandController | listProductsByBrandByPage | sortField : " + "name");
		LOGGER.info("ProductsByBrandController | listProductsByBrandByPage | sortDir : " + "asc");
		LOGGER.info("ProductsByBrandController | listProductsByBrandByPage | totalPages : " + pageProducts.getTotalPages());
		LOGGER.info("ProductsByBrandController | listProductsByBrandByPage | totalItems : " + pageProducts.getTotalElements());
		LOGGER.info("ProductsByBrandController | listProductsByBrandByPage | currentPage : " + pageNum);
		
		model.addAttribute("sortField", "name");
		model.addAttribute("sortDir", "asc");
		model.addAttribute("totalPages", pageProducts.getTotalPages());
		model.addAttribute("totalItems", pageProducts.getTotalElements());
		model.addAttribute("currentPage", pageNum);	
		

		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		
		LOGGER.info("ProductsByBrandController | listProductsByBrandByPage | startCount : " + startCount);
		model.addAttribute("startCount", startCount);

		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		LOGGER.info("ProductsByBrandController | listProductsByBrandByPage | endCount : " + endCount);
		
		if (endCount > pageProducts.getTotalElements()) {
			LOGGER.info("ProductsByBrandController | listProductsByBrandByPage | endCount > pageProducts.getTotalElements() ");
			endCount = pageProducts.getTotalElements();
			
		}

		model.addAttribute("endCount", endCount);
		LOGGER.info("ProductsByBrandController | listProductsByBrandByPage | endCount : " + endCount);

		LOGGER.info("ProductsByBrandController | listProductsByBrandByPage | brand : " + brand.toString());
		LOGGER.info("ProductsByBrandController | listProductsByBrandByPage | products size : " + listProducts.size());
		LOGGER.info("ProductsByBrandController | listProductsByBrandByPage | pageTitle : " + "Products by " + brand.getName());
		
		model.addAttribute("brand", brand);
		model.addAttribute("products", listProducts);
		model.addAttribute("pageTitle", "Products by " + brand.getName());		

		return "product/products_by_brand";
	}	
}
