package com.shopme.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.admin.service.BrandService;
import com.shopme.admin.service.ProductService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;

@Controller
public class ProductController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BrandController.class);
	
	private ProductService productService;
	
	private BrandService brandService;
	
	
	@Autowired
	public ProductController(ProductService productService, BrandService brandService) {
		super();
		this.productService = productService;
		this.brandService = brandService;
	}

	@GetMapping("/products")
	public String listAll(Model model) {
		
		LOGGER.info("BrandController | listAll is started");
		
		List<Product> listProducts = productService.listAll();
		
		LOGGER.info("BrandController | listAll | listProducts size : " + listProducts.size());

		model.addAttribute("listProducts", listProducts);

		return "products/products";
	}
	
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		
		LOGGER.info("BrandController | newProduct is started");
		
		List<Brand> listBrands = brandService.listAll();

		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		
		LOGGER.info("BrandController | newProduct | product : " + product);
		LOGGER.info("BrandController | newProduct | listBrands : " + listBrands.size());
		

		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Create New Product");

		return "products/product_form";
	}

	@PostMapping("/products/save")
	public String saveProduct(Product product) {
		
		LOGGER.info("BrandController | saveProduct is started");
		
		
		LOGGER.info("BrandController | saveProduct | Product Name : " + product.getName());
		LOGGER.info("BrandController | saveProduct | Brand ID: " + product.getBrand().getId());
		LOGGER.info("BrandController | saveProduct | Category ID: " + product.getCategory().getId());
		

		return "redirect:/products";
	}
}
