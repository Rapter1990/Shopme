package com.shopme.admin.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.helper.ProductSaveHelper;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.service.BrandService;
import com.shopme.admin.service.CategoryService;
import com.shopme.admin.service.ProductService;
import com.shopme.admin.util.AmazonS3Util;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductImage;
import com.shopme.common.exception.ProductNotFoundException;

@Controller
public class ProductController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
	
	private ProductService productService;
	
	private BrandService brandService;
	
	private CategoryService categoryService;
	
	private String defaultRedirectURL = "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";
	
	
	@Autowired
	public ProductController(ProductService productService, BrandService brandService,
			CategoryService categoryService) {
		super();
		this.productService = productService;
		this.brandService = brandService;
		this.categoryService = categoryService;
	}
	

	@GetMapping("/products")
	public String listFirstPage() {
		
		LOGGER.info("ProductController | listFirstPage is started");
		
		return defaultRedirectURL;
	}
	
	
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listProducts", moduleURL = "/products") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam("categoryId") Integer categoryId
			) {
		
		LOGGER.info("ProductController | listByPage is started");
		
		productService.listByPage(pageNum, helper, categoryId);
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();	
		
		if (categoryId != null) model.addAttribute("categoryId", categoryId);
		model.addAttribute("listCategories", listCategories);
		
		LOGGER.info("ProductController | newProduct | categoryId : " + categoryId);
		LOGGER.info("ProductController | newProduct | listCategories : " + listCategories.toString());

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
		model.addAttribute("numberOfExistingExtraImages", 0);

		return "products/product_form";
	}

	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes ra,
			@RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,			
			@RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser
			) throws IOException {
		
		LOGGER.info("ProductController | saveProduct is started");
		
		LOGGER.info("ProductController | saveProduct | mainImageMultipart.isEmpty() : " + mainImageMultipart.isEmpty());
		
		LOGGER.info("ProductController | saveProduct | extraImageMultiparts size : " + extraImageMultiparts.length);
		
		LOGGER.info("ProductController | saveProduct | loggedUser.getUsername() : " + loggedUser.getUsername());
		LOGGER.info("ProductController | saveProduct | loggedUser.getFullname() : " + loggedUser.getFullname());
		LOGGER.info("ProductController | saveProduct | loggedUser.getAuthorities() : " + loggedUser.getAuthorities());
		
		if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
			if (loggedUser.hasRole("Salesperson")) {
				productService.saveProductPrice(product);
				ra.addFlashAttribute("messageSuccess", "The product has been saved successfully.");			
				return defaultRedirectURL;
			}
		}
		
		ProductSaveHelper.setMainImageName(mainImageMultipart, product);
		
		ProductSaveHelper.setExistingExtraImageNames(imageIDs, imageNames, product);
		
		ProductSaveHelper.setNewExtraImageNames(extraImageMultiparts, product);
		
		ProductSaveHelper.setProductDetails(detailIDs, detailNames, detailValues, product);
		
		Product savedProduct = productService.save(product);
		
		/* Image Folder
		ProductSaveHelper.saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);
		ProductSaveHelper.deleteExtraImagesWeredRemovedOnForm(product);
		*/
		
		// Amazon S3 Image Storage
		ProductSaveHelper.saveUploadedImagesForAmazonS3(mainImageMultipart, extraImageMultiparts, savedProduct);
		ProductSaveHelper.deleteExtraImagesWeredRemovedOnFormForAmazonS3(product);
		

		ra.addFlashAttribute("messageSuccess", "The product has been saved successfully.");

		return defaultRedirectURL;
		
	}
	
	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		
		LOGGER.info("ProductController | updateCategoryEnabledStatus is started");
		
		productService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		
		LOGGER.info("ProductController | updateCategoryEnabledStatus | status : " + status);
		
		String message = "The Product ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("messageSuccess", message);

		return defaultRedirectURL;
	}
	
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		
		LOGGER.info("ProductController | deleteProduct is started");
		
		try {
			productService.delete(id);
			
			String productExtraImagesDir = "../product-images/" + id + "/extras";
			String productImagesDir = "../product-images/" + id;
			
			LOGGER.info("ProductController | deleteProduct | productExtraImagesDir : " + productExtraImagesDir);
			LOGGER.info("ProductController | deleteProduct | productImagesDir : " + productImagesDir);

			// Image Folder
			//FileUploadUtil.removeDir(productExtraImagesDir);
			//FileUploadUtil.removeDir(productImagesDir);
			 
            // Amazon S3 option
            AmazonS3Util.removeFolder(productExtraImagesDir);
            AmazonS3Util.removeFolder(productExtraImagesDir);
			
			LOGGER.info("ProductController | deleteProduct is done");
			
			redirectAttributes.addFlashAttribute("messageSuccess", 
					"The product ID " + id + " has been deleted successfully");
		} catch (ProductNotFoundException ex) {
			
			LOGGER.info("ProductController | deleteProduct | messageError : " + ex.getMessage());
			
			redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
		}

		return defaultRedirectURL;
	}
	
	
	
	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser
			) {
		
		LOGGER.info("ProductController | editProduct is started");
		
		try {
			Product product = productService.get(id);
			List<Brand> listBrands = brandService.listAll();
			Integer numberOfExistingExtraImages = product.getImages().size();
			
			LOGGER.info("ProductController | editProduct | loggedUser  : " + loggedUser.toString());
			
			boolean isReadOnlyForSalesperson = false;

			if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
				if (loggedUser.hasRole("Salesperson")) {
					isReadOnlyForSalesperson = true;
				}
			}

			
			LOGGER.info("ProductController | editProduct | product  : " + product.toString());
			LOGGER.info("ProductController | editProduct | listBrands : " + listBrands.toString());
			LOGGER.info("ProductController | editProduct | numberOfExistingExtraImages : " + numberOfExistingExtraImages);
			LOGGER.info("ProductController | editProduct | isReadOnlyForSalesperson  : " + isReadOnlyForSalesperson);

			model.addAttribute("isReadOnlyForSalesperson", isReadOnlyForSalesperson);
			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);


			return "products/product_form";

		} catch (ProductNotFoundException e) {
			
			LOGGER.info("ProductController | editProduct | error : " + e.getMessage());
			
			ra.addFlashAttribute("messageError", e.getMessage());

			return defaultRedirectURL;
		}
	}
	
	
	@GetMapping("/products/detail/{id}")
	public String viewProductDetails(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra) {
		
		LOGGER.info("ProductController | viewProductDetails is started");
		
		try {
			Product product = productService.get(id);
			
			LOGGER.info("ProductController | viewProductDetails  | product : " + product.toString());
			
			model.addAttribute("product", product);		

			return "products/product_detail_modal";

		} catch (ProductNotFoundException e) {
			
			LOGGER.info("ProductController | viewProductDetails  | messageError : " + e.getMessage());
			
			ra.addFlashAttribute("messageError", e.getMessage());

			return defaultRedirectURL;
		}
	}	

}
