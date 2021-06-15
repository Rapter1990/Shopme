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
import com.shopme.common.entity.ProductImage;

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
		model.addAttribute("numberOfExistingExtraImages", 0);

		return "products/product_form";
	}

	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes ra,
			@RequestParam("fileImage") MultipartFile mainImageMultipart,
			@RequestParam("extraImage") MultipartFile[] extraImageMultiparts,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames
			) throws IOException {
		
		LOGGER.info("ProductController | saveProduct is started");
		
		LOGGER.info("ProductController | saveProduct | mainImageMultipart.isEmpty() : " + mainImageMultipart.isEmpty());
		
		LOGGER.info("ProductController | saveProduct | extraImageMultiparts size : " + extraImageMultiparts.length);
		
		setMainImageName(mainImageMultipart, product);
		
		setExistingExtraImageNames(imageIDs, imageNames, product);
		
		setNewExtraImageNames(extraImageMultiparts, product);
		
		setProductDetails(detailIDs, detailNames, detailValues, product);
		
		Product savedProduct = productService.save(product);

		saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);
		
		deleteExtraImagesWeredRemovedOnForm(product);

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
			
			String productExtraImagesDir = "../product-images/" + id + "/extras";
			String productImagesDir = "../product-images/" + id;
			
			LOGGER.info("ProductController | deleteProduct | productExtraImagesDir : " + productExtraImagesDir);
			LOGGER.info("ProductController | deleteProduct | productImagesDir : " + productImagesDir);

			FileUploadUtil.removeDir(productExtraImagesDir);
			
			FileUploadUtil.removeDir(productImagesDir);
			
			LOGGER.info("ProductController | deleteProduct is done");
			
			redirectAttributes.addFlashAttribute("messageSuccess", 
					"The product ID " + id + " has been deleted successfully");
		} catch (ProductNotFoundException ex) {
			
			LOGGER.info("ProductController | deleteProduct | messageError : " + ex.getMessage());
			
			redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
		}

		return "redirect:/products";
	}
	
	private void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
		
		LOGGER.info("ProductController | setNewExtraImageNames is started");
		
		LOGGER.info("ProductController | setNewExtraImageNames | extraImageMultiparts.length : " + extraImageMultiparts.length);
		
		if (extraImageMultiparts.length > 0) {
			
			for (MultipartFile multipartFile : extraImageMultiparts) {
				
				LOGGER.info("ProductController | setNewExtraImageNames | !multipartFile.isEmpty() : " + !multipartFile.isEmpty());
				
				if (!multipartFile.isEmpty()) {
					
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					
					LOGGER.info("ProductController | setNewExtraImageNames | fileName : " + fileName);
					
					
					if (!product.containsImageName(fileName)) {
						product.addExtraImage(fileName);
					}
					
					
				}
			}
		}
		
		LOGGER.info("ProductController | setExtraImageNames is completed");
	}

	private void setMainImageName(MultipartFile mainImageMultipart, Product product) {
		
		LOGGER.info("ProductController | setMainImageName is started");
		
		LOGGER.info("ProductController | setMainImageName | !mainImageMultipart.isEmpty() : " + !mainImageMultipart.isEmpty());
		
		if (!mainImageMultipart.isEmpty()) {
			
			
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			
			LOGGER.info("ProductController | setMainImageName | fileName : " + fileName);
			
			product.setMainImage(fileName);
				
		}
		
		
		LOGGER.info("ProductController | setMainImageName is completed");
	}
	
	private void saveUploadedImages(MultipartFile mainImageMultipart, 
			MultipartFile[] extraImageMultiparts, Product savedProduct) throws IOException {
		
		LOGGER.info("ProductController | saveUploadedImages is started");
		
		LOGGER.info("ProductController | setMainImageName | !mainImageMultipart.isEmpty() : " + !mainImageMultipart.isEmpty());
		
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			
			LOGGER.info("ProductController | setMainImageName | fileName : " + fileName);
			
			String uploadDir = "../product-images/" + savedProduct.getId();
			
			LOGGER.info("ProductController | setMainImageName | uploadDir : " + uploadDir);

			FileUploadUtil.cleanDir(uploadDir);
			
			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
		}
		
		LOGGER.info("ProductController | setMainImageName | extraImageMultiparts.length : " + extraImageMultiparts.length);
		
		if (extraImageMultiparts.length > 0) {
			
			String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";
			
			LOGGER.info("ProductController | setMainImageName | uploadDir : " + uploadDir);

			for (MultipartFile multipartFile : extraImageMultiparts) {
				
				LOGGER.info("ProductController | setMainImageName | multipartFile.isEmpty() : " + multipartFile.isEmpty());
				if (multipartFile.isEmpty()) continue;

				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				
				LOGGER.info("ProductController | setMainImageName | fileName : " + fileName);
				
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
				
			}
		}
		
		
		LOGGER.info("ProductController | saveUploadedImages is completed");
	}
	
	private void setProductDetails(String[] detailIDs, String[] detailNames, 
			String[] detailValues, Product product) {
		
		LOGGER.info("ProductController | setProductDetails is started");
		
		LOGGER.info("ProductController | setProductDetails | detailNames : " + detailNames.toString());
		LOGGER.info("ProductController | setProductDetails | detailNames : " + detailValues.toString());
		LOGGER.info("ProductController | setProductDetails | product : " + product.toString());
		
		
		if (detailNames == null || detailNames.length == 0) return;

		for (int count = 0; count < detailNames.length; count++) {
			String name = detailNames[count];
			String value = detailValues[count];
			Integer id = Integer.parseInt(detailIDs[count]);

			if (id != 0) {
				product.addDetail(id, name, value);
			} else if (!name.isEmpty() && !value.isEmpty()) {
				product.addDetail(name, value);
			}
		}
		
		LOGGER.info("ProductController | setProductDetails | product with its detail : " + product.getDetails().toString());
		
		LOGGER.info("ProductController | setProductDetails is completed");
	}
	
	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra) {
		
		LOGGER.info("ProductController | editProduct is started");
		
		try {
			Product product = productService.get(id);
			List<Brand> listBrands = brandService.listAll();
			Integer numberOfExistingExtraImages = product.getImages().size();
			
			LOGGER.info("ProductController | editProduct | product  : " + product.toString());
			LOGGER.info("ProductController | editProduct | listBrands : " + listBrands.toString());
			LOGGER.info("ProductController | editProduct | numberOfExistingExtraImages : " + numberOfExistingExtraImages);

			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);


			return "products/product_form";

		} catch (ProductNotFoundException e) {
			
			LOGGER.info("ProductController | editProduct | error : " + e.getMessage());
			
			ra.addFlashAttribute("messageError", e.getMessage());

			return "redirect:/products";
		}
	}
	
	private void deleteExtraImagesWeredRemovedOnForm(Product product) {
		
		LOGGER.info("ProductController | deleteExtraImagesWeredRemovedOnForm is started");
		
		String extraImageDir = "../product-images/" + product.getId() + "/extras";
		Path dirPath = Paths.get(extraImageDir);
		
		LOGGER.info("ProductController | deleteExtraImagesWeredRemovedOnForm | dirPath  : " + dirPath);
		

		try {
			Files.list(dirPath).forEach(file -> {
				String filename = file.toFile().getName();

				if (!product.containsImageName(filename)) {
					try {
						Files.delete(file);
						LOGGER.info("Deleted extra image: " + filename);

					} catch (IOException e) {
						LOGGER.error("Could not delete extra image: " + filename);
					}
				}

			});
		} catch (IOException ex) {
			LOGGER.error("Could not list directory: " + dirPath);
		}
	}

	private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, 
			Product product) {
		
		LOGGER.info("ProductController | setExistingExtraImageNames is started");
		
		LOGGER.info("ProductController | deleteExtraImagesWeredRemovedOnForm | imageIDs  : " + imageIDs.toString());
		LOGGER.info("ProductController | deleteExtraImagesWeredRemovedOnForm | imageNames  : " + imageNames.toString());
		
		if (imageIDs == null || imageIDs.length == 0) return;

		Set<ProductImage> images = new HashSet<>();

		for (int count = 0; count < imageIDs.length; count++) {
			Integer id = Integer.parseInt(imageIDs[count]);
			String name = imageNames[count];

			images.add(new ProductImage(id, name, product));
		}

		product.setImages(images);

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

			return "redirect:/products";
		}
	}	

}
