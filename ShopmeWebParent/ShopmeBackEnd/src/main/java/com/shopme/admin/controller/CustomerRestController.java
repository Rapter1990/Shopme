package com.shopme.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.service.CustomerService;

@RestController
public class CustomerRestController {
	
	@Autowired
	private CustomerService service;

	@PostMapping("/customers/check_email")
	public String checkDuplicateEmail(@RequestParam("id") Integer id, @RequestParam("email") String email) {
		if (service.isEmailUnique(id, email)) {
			return "OK";
		} else {
			return "Duplicated";
		}
	}
}
