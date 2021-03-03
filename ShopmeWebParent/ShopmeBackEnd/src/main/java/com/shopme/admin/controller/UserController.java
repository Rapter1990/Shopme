package com.shopme.admin.controller;

import java.io.IOException;
import java.util.List;

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

import com.shopme.admin.error.UserNotFoundException;
import com.shopme.admin.service.UserService;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class UserController {

	@Autowired
	private UserService service;
	
	@GetMapping("/users")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "firstName", "asc", null);
	}
	
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		
		System.out.println("Sort Field: " + sortField);
		System.out.println("Sort Order: " + sortDir);

		Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		
	
		List<User> listUsers = page.getContent();

		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
		
		System.out.println("startCount : " + startCount);
		System.out.println("endCount : " + endCount);
		
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		System.out.println("page.getTotalElements() : " + page.getTotalElements());
		System.out.println("endCount (endCount > page.getTotalElements()): " + endCount);
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);

		return "users";	
	}
	
	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = service.listRoles();
		
		User user = new User();
		user.setEnabled(true);
		
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");
		
		return "user_form";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes, 
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		

		if (!multipartFile.isEmpty()) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				user.setPhotos(fileName);
				User savedUser = service.save(user);
	
				String uploadDir = "user-photos/" + savedUser.getId();
	
				FileUploadUtil.cleanDir(uploadDir);
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
	
		} else {
			if (user.getPhotos().isEmpty()) user.setPhotos(null);
			service.save(user);
		}
		
		
		redirectAttributes.addFlashAttribute("messageSuccess", "The user has been saved successfully.");
		
		return "redirect:/users";
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			User user = service.get(id);
			List<Role> listRoles = service.listRoles();
			
			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
			model.addAttribute("listRoles", listRoles);
			
			return "user_form";
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
			return "redirect:/users";
		}
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("messageSuccess", 
					"The user ID " + id + " has been deleted successfully");
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
		}
		
		return "redirect:/users";
	}
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		service.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The user ID " + id + " has been " + status;
		
		if(message.contains("enabled")) {
			redirectAttributes.addFlashAttribute("messageSuccess", message);
		}else {
			redirectAttributes.addFlashAttribute("messageError", message);
		}
		
		
		return "redirect:/users";
	}
}
