package com.shopme.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.service.CustomerService;
import com.shopme.service.SettingService;
import com.shopme.util.CustomerForgetPasswordUtil;
import com.shopme.util.CustomerRegisterUtil;

@Controller
public class ForgotPasswordController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ForgotPasswordController.class);
	
	private CustomerService customerService;
	
	private SettingService settingService;
	
	@Autowired
	public ForgotPasswordController(CustomerService customerService, SettingService settingService) {
		super();
		this.customerService = customerService;
		this.settingService = settingService;
	}

	@GetMapping("/forgot_password")
	public String showRequestForm() {
		
		LOGGER.info("ForgotPasswordController | showRequestForm is called");
		
		return "customer/forgot_password_form";
	}

	@PostMapping("/forgot_password")
	public String processRequestForm(HttpServletRequest request, Model model) {
		
		LOGGER.info("ForgotPasswordController | processRequestForm is called");
		
		String email = request.getParameter("email");
		
		LOGGER.info("ForgotPasswordController | processRequestForm | email : " + email);
		
		try {
			String token = customerService.updateResetPasswordToken(email);
			
			LOGGER.info("ForgotPasswordController | processRequestForm | token : " + token);
			
			String link = CustomerRegisterUtil.getSiteURL(request) + "/reset_password?token=" + token;
			
			LOGGER.info("ForgotPasswordController | processRequestForm | link : " + link);
			
			CustomerForgetPasswordUtil.sendEmail(link, email, settingService);

			model.addAttribute("message", "We have sent a reset password link to your email."
					+ " Please check.");
		} catch (CustomerNotFoundException e) {
			
			LOGGER.info("ForgotPasswordController | processRequestForm | error : " + e.getMessage());
			
			model.addAttribute("error", e.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			
			LOGGER.info("ForgotPasswordController | processRequestForm | error : " + "Could not send email");
			
			model.addAttribute("error", "Could not send email");
		}

		return "customer/forgot_password_form";
	}


	@GetMapping("/reset_password")
	public String showResetForm(@RequestParam("token") String token, Model model) {
		
		LOGGER.info("ForgotPasswordController | showResetForm is called");
		
		Customer customer = customerService.getByResetPasswordToken(token);
		
		LOGGER.info("ForgotPasswordController | showResetForm | customer : " + customer.toString());
		
		if (customer != null) {
			
			LOGGER.info("ForgotPasswordController | showResetForm | token : " + token);
			
			model.addAttribute("token", token);
		} else {
			
			model.addAttribute("pageTitle", "Invalid Token");
			model.addAttribute("message", "Invalid Token");
			
			LOGGER.info("ForgotPasswordController | showResetForm | pageTitle : " + "Invalid Token");
			LOGGER.info("ForgotPasswordController | showResetForm | message : " + "Invalid Token");
			
			return "message";
		}

		return "customer/reset_password_form";
	}

	@PostMapping("/reset_password")
	public String processResetForm(HttpServletRequest request, Model model) {
		
		LOGGER.info("ForgotPasswordController | processResetForm is called");
		
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		
		
		LOGGER.info("ForgotPasswordController | processResetForm | token : " + token);
		LOGGER.info("ForgotPasswordController | processResetForm | password : " + password);

		try {
			customerService.updatePassword(token, password);

			model.addAttribute("pageTitle", "Reset Your Password");
			model.addAttribute("title", "Reset Your Password");
			model.addAttribute("message", "You have successfully changed your password.");
			
			LOGGER.info("ForgotPasswordController | processResetForm | pageTitle : " + "Reset Your Password");
			LOGGER.info("ForgotPasswordController | processResetForm | title : " + "Reset Your Password");
			LOGGER.info("ForgotPasswordController | processResetForm | message : " + "You have successfully changed your password.");
			

		} catch (CustomerNotFoundException e) {
			model.addAttribute("pageTitle", "Invalid Token");
			model.addAttribute("message", e.getMessage());
			
			LOGGER.info("ForgotPasswordController | processResetForm | pageTitle : " + "Invalid Token");
			LOGGER.info("ForgotPasswordController | processResetForm | message : " + e.getMessage());
			
		}	

		return "message";		
	}
}
