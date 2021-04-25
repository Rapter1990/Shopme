package com.shopme.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.admin.service.BrandService;
import com.shopme.common.entity.Brand;

@Controller
public class BrandController {

	@Autowired
	private BrandService service;

	@GetMapping("/brands")
	public String listAll(Model model) {
		List<Brand> listBrands = service.listAll();
		model.addAttribute("listBrands", listBrands);

		return "brands/brands";
	}
	
}
