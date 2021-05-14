package com.shopme.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.shopme.admin.error.BrandNotFoundException;
import com.shopme.admin.exportcsv.BrandCsvExporter;
import com.shopme.admin.exportcsv.CategoryCsvExporter;
import com.shopme.admin.exportexcel.BrandExcelExporter;
import com.shopme.admin.exportexcel.CategoryExcelExporter;
import com.shopme.admin.exportpdf.BrandPdfExporter;
import com.shopme.admin.exportpdf.CategoryPdfExporter;
import com.shopme.admin.service.BrandService;
import com.shopme.admin.service.CategoryService;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@Controller
public class BrandController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BrandController.class);

	private BrandService brandService;
	
	private CategoryService categoryService;
	
	@Autowired
	public BrandController(BrandService brandService, CategoryService categoryService) {
		super();
		this.brandService = brandService;
		this.categoryService = categoryService;
	}


	@GetMapping("/brands")
	public String listFirstPage(Model model) {
		
		LOGGER.info("BrandController | listByPage is started");
		
		return listByPage(1, model, "name", "asc", null);
	}
	
	@GetMapping("/brands/new")
	public String newBrand(Model model) {
		
		LOGGER.info("BrandController | newBrand is started");
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTitle", "Create New Brand");
		
		LOGGER.info("CategoryController | newBrand | listCategories : " + listCategories.toString());

		return "brands/brand_form";		
	}
	
	@PostMapping("/brands/save")
	public String saveBrand(Brand brand, @RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException {
		
		LOGGER.info("BrandController | saveBrand is started");
		
		LOGGER.info("BrandController | saveBrand | multipartFile.isEmpty() : " + multipartFile.isEmpty());
		
		
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			
			LOGGER.info("BrandController | saveCategory | fileName : " + fileName);
			
			brand.setLogo(fileName);

			Brand savedBrand = brandService.save(brand);
			String uploadDir = "../brand-logos/" + savedBrand.getId();
			
			LOGGER.info("BrandController | saveBrand | savedBrand : " + savedBrand.toString());
			LOGGER.info("BrandController | saveBrand | uploadDir : " + uploadDir);

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		} else {
			brandService.save(brand);
		}

		ra.addFlashAttribute("messageSuccess", "The brand has been saved successfully.");
		return "redirect:/brands";		
	}

	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes ra) {
		
		LOGGER.info("BrandController | editBrand is started");
		
		try {
			Brand brand = brandService.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			
			LOGGER.info("BrandController | editBrand | brand : " + brand.toString());
			LOGGER.info("BrandController | editBrand | listCategories : " + listCategories.toString());

			model.addAttribute("brand", brand);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Edit Brand (ID: " + id + ")");

			return "brands/brand_form";			
		} catch (BrandNotFoundException ex) {
			ra.addFlashAttribute("messageError", ex.getMessage());
			return "redirect:/brands";
		}
	}

	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		
		LOGGER.info("BrandController | deleteBrand is started");
		
		LOGGER.info("BrandController | deleteBrand | id : " + id);
		
		try {
			brandService.delete(id);
			
			LOGGER.info("BrandController | deleteBrand | brand deleted");
			
			String brandDir = "../brand-logos/" + id;
			
			LOGGER.info("BrandController | deleteBrand | brandDir : " + brandDir);
			
			FileUploadUtil.removeDir(brandDir);
			
			LOGGER.info("BrandController | deleteBrand | FileUploadUtil.removeDir is over");
			

			redirectAttributes.addFlashAttribute("messageSuccess", 
					"The brand ID " + id + " has been deleted successfully");
		} catch (BrandNotFoundException ex) {
			redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
		}

		return "redirect:/brands";
	}	
	
	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword
			) {
		
		LOGGER.info("BrandController | listByPage is started");
		
		Page<Brand> page = brandService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Brand> listBrands = page.getContent();
		
		LOGGER.info("BrandController | listByPage | page : " + page.getSize());
		LOGGER.info("BrandController | listByPage | listBrands : " + listBrands.size());

		long startCount = (pageNum - 1) * BrandService.BRANDS_PER_PAGE + 1;
		long endCount = startCount + BrandService.BRANDS_PER_PAGE - 1;
		
		LOGGER.info("BrandController | listByPage | startCount : " + startCount);
		LOGGER.info("BrandController | listByPage | endCount : " + endCount);
		
		LOGGER.info("BrandController | listByPage | page.getTotalElements() : " + page.getTotalElements());
		
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		
		LOGGER.info("BrandController | listByPage | reverseSortDir : " + reverseSortDir);

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);		
		model.addAttribute("listBrands", listBrands);
		
		LOGGER.info("BrandController | listByPage | currentPage : " + pageNum);
		LOGGER.info("BrandController | listByPage | totalPages : " + page.getTotalPages());
		LOGGER.info("BrandController | listByPage | totalItems : " + page.getTotalElements() );
		LOGGER.info("BrandController | listByPage | sortField : " + sortField );
		LOGGER.info("BrandController | listByPage | sortDir : " + sortDir );
		LOGGER.info("BrandController | listByPage | keyword : " + keyword);
		LOGGER.info("BrandController | listByPage | listBrands : " + listBrands.size());

		return "brands/brands";		
	}
	
	@GetMapping("/brands/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		
		LOGGER.info("BrandController | exportToCSV is started");
		
		List<Brand> listBrands = brandService.listAll();
		
		LOGGER.info("BrandController | exportToCSV | listBrands : " + listBrands.toString());
		
		BrandCsvExporter exporter = new BrandCsvExporter();
		exporter.export(listBrands, response);
		
		LOGGER.info("BrandController | exportToCSV | export completed");
		
	}
	
	@GetMapping("/brands/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		
		LOGGER.info("BrandController | exportToExcel is called");
		
		List<Brand> listBrands = brandService.listAll();
		
		LOGGER.info("BrandController | exportToExcel | listBrands Size : " + listBrands.size());

		BrandExcelExporter exporter = new BrandExcelExporter();
		
		LOGGER.info("BrandController | exportToExcel | export is starting");
		
		exporter.export(listBrands, response);
		
		LOGGER.info("BrandController | exportToExcel | export completed");
		
	}
	
	@GetMapping("/brands/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		
		LOGGER.info("BrandController | exportToPDF is called");
		
		List<Brand> listBrands = brandService.listAll();
		
		LOGGER.info("BrandController | exportToPDF | listBrands Size : " + listBrands.size());
		
		BrandPdfExporter exporter = new BrandPdfExporter();
		
		LOGGER.info("BrandController | exportToPDF | export is starting");
		
		exporter.export(listBrands, response);
		
		LOGGER.info("BrandController | exportToPDF | export completed");
		
	}
	
}
