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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.error.CustomerNotFoundException;
import com.shopme.admin.exportcsv.CustomerCsvExporter;
import com.shopme.admin.exportexcel.CustomerExcelExporter;
import com.shopme.admin.exportpdf.CustomerPdfExporter;
import com.shopme.admin.service.CustomerService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@Controller
public class CustomerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired 
	private CustomerService service;
	
	@GetMapping("/customers")
	public String listFirstPage(Model model) {
		
		LOGGER.info("CustomerController | listFirstPage is called");
		
		return listByPage(model, 1, "firstName", "asc", null);
	}

	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(Model model, 
						@PathVariable(name = "pageNum") int pageNum,
						@Param("sortField") String sortField,
						@Param("sortDir") String sortDir,
						@Param("keyword") String keyword
			) {

		Page<Customer> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<Customer> listCustomers = page.getContent();

		long startCount = (pageNum - 1) * CustomerService.CUSTOMERS_PER_PAGE + 1;
		model.addAttribute("startCount", startCount);
		
		LOGGER.info("CustomerController | viewDetails | startCount : " + startCount);
		

		long endCount = startCount + CustomerService.CUSTOMERS_PER_PAGE - 1;
		
		LOGGER.info("CustomerController | listByPage | endCount : " + endCount);
		LOGGER.info("CustomerController | listByPage | page.getTotalElements() : " + page.getTotalElements());
		
		
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("listCustomers", listCustomers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("endCount", endCount);
		
		LOGGER.info("CustomerController | listByPage | endCount : " + endCount);
		LOGGER.info("CustomerController | listByPage | totalPages : " + page.getTotalPages());
		LOGGER.info("CustomerController | listByPage | totalItems : " + page.getTotalElements());
		LOGGER.info("CustomerController | listByPage | currentPage : " + pageNum);
		LOGGER.info("CustomerController | listByPage | listCustomers : " + listCustomers);
		LOGGER.info("CustomerController | listByPage | sortField : " + sortField);
		LOGGER.info("CustomerController | listByPage | sortDir : " + sortDir);
		LOGGER.info("CustomerController | listByPage | keyword : " + keyword);
		LOGGER.info("CustomerController | listByPage | reverseSortDir : " + (sortDir.equals("asc") ? "desc" : "asc") );
		LOGGER.info("CustomerController | listByPage | endCount : " + endCount);

		return "customers/customers";
	}

	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateCustomerEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		
		LOGGER.info("CustomerController | updateCustomerEnabledStatus is called");
		
		service.updateCustomerEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Customer ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		
		LOGGER.info("CustomerController | listByPage | status : " + status);
		LOGGER.info("CustomerController | listByPage | message : " + message);
		

		return "redirect:/customers";
	}	

	@GetMapping("/customers/detail/{id}")
	public String viewCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		
		LOGGER.info("CustomerController | viewCustomer is called");
		
		try {
			Customer customer = service.get(id);
			model.addAttribute("customer", customer);

			LOGGER.info("CustomerController | viewCustomer | customer : " + customer.toString());
			
			return "customers/customer_detail_modal";
		} catch (CustomerNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			
			LOGGER.info("CustomerController | viewCustomer | message : " + ex.getMessage());
			
			return "redirect:/customers";			
		}
	}

	@GetMapping("/customers/edit/{id}")
	public String editCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		
		LOGGER.info("CustomerController | editCustomer is called");
		
		try {
			Customer customer = service.get(id);
			List<Country> countries = service.listAllCountries();

			model.addAttribute("listCountries", countries);			
			model.addAttribute("customer", customer);
			model.addAttribute("pageTitle", String.format("Edit Customer (ID: %d)", id));
			
			LOGGER.info("CustomerController | editCustomer | listCountries : " + countries);
			LOGGER.info("CustomerController | editCustomer | customer : " + customer);
			LOGGER.info("CustomerController | editCustomer | pageTitle : " + (String.format("Edit Customer (ID: %d)", id)));

			return "customers/customer_form";

		} catch (CustomerNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			
			LOGGER.info("CustomerController | editCustomer | message : " + ex.getMessage());
			
			return "redirect:/customers";
		}
	}

	@PostMapping("/customers/save")
	public String saveCustomer(Customer customer, Model model, RedirectAttributes ra) {
		
		LOGGER.info("CustomerController | saveCustomer is called");
		
		service.save(customer);
		ra.addFlashAttribute("message", "The customer ID " + customer.getId() + " has been updated successfully.");
		
		LOGGER.info("CustomerController | editCustomer | message : " + "The customer ID " + customer.getId() + " has been updated successfully.");
		
		return "redirect:/customers";
	}

	@GetMapping("/customers/delete/{id}")
	public String deleteCustomer(@PathVariable Integer id, RedirectAttributes ra) {
		
		LOGGER.info("CustomerController | deleteCustomer is called");
		
		try {
			service.delete(id);			
			ra.addFlashAttribute("message", "The customer ID " + id + " has been deleted successfully.");
			
			LOGGER.info("CustomerController | deleteCustomer | message : " + "The customer ID " + id + " has been deleted successfully.");

		} catch (CustomerNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			
			LOGGER.info("CustomerController | deleteCustomer | message : " + ex.getMessage());
		}

		return "redirect:/customers";
	}
	
	@GetMapping("/customers/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		
		LOGGER.info("CustomerController | exportToCSV is called");
		
		List<Customer> listCustomers = service.listAll();
		
		LOGGER.info("CustomerController | exportToCSV | listCustomers.size() : " + listCustomers.size());
		
		CustomerCsvExporter exporter = new CustomerCsvExporter();
		
		LOGGER.info("CustomerController | exportToCSV | export is starting");
		
		exporter.export(listCustomers, response);
		
		LOGGER.info("CustomerController | exportToCSV | export completed");
	}
	
	@GetMapping("/customers/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		
		LOGGER.info("CustomerController | exportToExcel is called");
		
		List<Customer> listCustomers = service.listAll();
		
		LOGGER.info("CustomerController | exportToExcel | listCustomers.size() : " + listCustomers.size());

		CustomerExcelExporter exporter = new CustomerExcelExporter();
		
		LOGGER.info("CustomerController | exportToExcel | export is starting");
		
		exporter.export(listCustomers, response);
		
		LOGGER.info("CustomerController | exportToExcel | export completed");
	}
	
	@GetMapping("/customers/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		
		LOGGER.info("CustomerController | exportToPDF is called");
		
		List<Customer> listCustomers = service.listAll();
		
		LOGGER.info("UserController | exportToPDF | listCustomers.size() : " + listCustomers.size());
		
		CustomerPdfExporter exporter = new CustomerPdfExporter();
		
		LOGGER.info("UserController | exportToPDF | export is starting");
		
		exporter.export(listCustomers, response);
		
		LOGGER.info("CustomerController | exportToPDF | export completed");
	}	
}
