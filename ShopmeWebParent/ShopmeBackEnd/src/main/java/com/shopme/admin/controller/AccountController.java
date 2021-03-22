package com.shopme.admin.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.service.UserService;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.User;


@Controller
public class AccountController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
	
	@Autowired
	private UserService service;

	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal ShopmeUserDetails loggedUser,
			Model model) {
		
		LOGGER.info("AccountController | viewDetails is called");
		
		String email = loggedUser.getUsername();
		
		LOGGER.info("AccountController | viewDetails | email : " + email);
		
		User user = service.getByEmail(email);
		
		LOGGER.info("AccountController | viewDetails | user : " + user.toString());
		
		model.addAttribute("user", user);
		
		return "users/account_form";

	}
	
	@PostMapping("/account/update")
	public String saveDetails(User user, RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		
		LOGGER.info("AccountController | saveDetails is called");
		
		LOGGER.info("AccountController | saveDetails | multipartFile : " + multipartFile);

		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			
			LOGGER.info("AccountController | saveDetails | fileName : " + fileName);
			
			user.setPhotos(fileName);
			User savedUser = service.updateAccount(user);
			
			LOGGER.info("AccountController | saveDetails | savedUser : " + savedUser.toString());

			String uploadDir = "user-photos/" + savedUser.getId();
			
			LOGGER.info("AccountController | saveDetails | uploadDir : " + uploadDir);

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		} else {
			LOGGER.info("AccountController | saveDetails | user.getPhotos() : " + user.getPhotos());
			
			if (user.getPhotos().isEmpty()) user.setPhotos(null);
			
			service.updateAccount(user);
			
			LOGGER.info("AccountController | saveDetails | Update completed");
		}

		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());
		
		LOGGER.info("AccountController | saveDetails | loggedUser : " + loggedUser.toString());

		redirectAttributes.addFlashAttribute("message", "Your account details have been updated.");

		return "redirect:/account";
	}	
}
