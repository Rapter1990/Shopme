package com.shopme.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.service.ProductService;

@RestController
public class ProductRestController {

	@Autowired 
	private ProductService service;

	@PostMapping("/products/check_unique")
	public String checkUnique(@RequestParam("id") Integer id, @RequestParam("name") String name) {
		return service.checkUnique(id, name);
	}	
}