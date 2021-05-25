package com.shopme.admin.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.error.ProductNotFoundException;
import com.shopme.admin.service.BrandService;
import com.shopme.admin.service.ProductService;
import com.shopme.admin.util.FileUploadUtil;
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
		
		LOGGER.info("ProductController | listAll is started");
		
		List<Product> listProducts = productService.listAll();
		
		LOGGER.info("ProductController | listAll | listProducts size : " + listProducts.size());

		model.addAttribute("listProducts", listProducts);

		return "products/products";
	}
	
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		
		LOGGER.info("ProductController | newProduct is started");
		
		List<Brand> listBrands = brandService.listAll();

		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		
		LOGGER.info("ProductController | newProduct | product : " + product);
		LOGGER.info("ProductController | newProduct | listBrands : " + listBrands.size());
		

		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Create New Product");

		return "products/product_form";
	}

	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes ra,
			@RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
		
		LOGGER.info("ProductController | saveProduct is started");
		
		LOGGER.info("ProductController | saveProduct | multipartFile.isEmpty() : " + multipartFile.isEmpty());
		
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			
			LOGGER.info("ProductController | saveProduct | fileName : " + fileName);
			
			product.setMainImage(fileName);

			Product savedProduct = productService.save(product);
			
			
			String uploadDir = "../product-images/" + savedProduct.getId();
			
			LOGGER.info("ProductController | saveProduct | uploadDir : " + uploadDir);

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		} else {
			
			LOGGER.info("ProductController | saveProduct |  No Image ");
			
			productService.save(product);
		}
		
		ra.addFlashAttribute("messageSuccess", "The product has been saved successfully.");
		

		return "redirect:/products";
	}
	
	@GetMapping("/products/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		
		LOGGER.info("ProductController | updateCategoryEnabledStatus is started");
		
		productService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		
		LOGGER.info("ProductController | updateCategoryEnabledStatus | status : " + status);
		
		String message = "The Product ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("messageSuccess", message);

		return "redirect:/products";
	}
	
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		
		LOGGER.info("ProductController | deleteProduct is started");
		
		try {
			productService.delete(id);
			
			LOGGER.info("ProductController | deleteProduct is done");
			
			redirectAttributes.addFlashAttribute("messageSuccess", 
					"The product ID " + id + " has been deleted successfully");
		} catch (ProductNotFoundException ex) {
			
			LOGGER.info("ProductController | deleteProduct | messageError : " + ex.getMessage());
			
			redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
		}

		return "redirect:/products";
	}
}
