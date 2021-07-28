package com.shopme.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.service.CustomerService;
import com.shopme.service.SettingService;
import com.shopme.util.CustomerRegisterUtil;

@Controller
public class CustomerController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired 
	private CustomerService customerService;
	
	@Autowired 
	private SettingService settingService;

	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		
		LOGGER.info("CustomerRegisterUtil | showRegisterForm is called");
		
		List<Country> listCountries = customerService.listAllCountries();

		LOGGER.info("CustomerRegisterUtil | listCountries : " + listCountries.toString());
		
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("pageTitle", "Customer Registration");
		model.addAttribute("customer", new Customer());

		return "register/register_form";
	}
	
	@PostMapping("/create_customer")
	public String createCustomer(Customer customer, Model model,
			HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		
		LOGGER.info("CustomerRegisterUtil | createCustomer is called");
		
		customerService.registerCustomer(customer);
		
		LOGGER.info("CustomerRegisterUtil | createCustomer | customer : " + customer);
		
		CustomerRegisterUtil.sendVerificationEmail(request, customer, settingService);

		model.addAttribute("pageTitle", "Registration Succeeded!");

		return "/register/register_success";
	}
}
