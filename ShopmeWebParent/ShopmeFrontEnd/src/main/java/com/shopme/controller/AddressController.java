package com.shopme.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.service.AddressService;
import com.shopme.service.CustomerService;
import com.shopme.util.AuthenticationControllerHelperUtil;

@Controller
public class AddressController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AddressController.class);
 
	private AddressService addressService;
	
	private CustomerService customerService;
	
	private AuthenticationControllerHelperUtil authenticationControllerHelperUtil;
	
	@Autowired
	public AddressController(AddressService addressService, 
			                 CustomerService customerService,
			                 AuthenticationControllerHelperUtil authenticationControllerHelperUtil) {
		super();
		this.addressService = addressService;
		this.customerService = customerService;
		this.authenticationControllerHelperUtil = authenticationControllerHelperUtil;
	}

	@GetMapping("/address_book")
	public String showAddressBook(Model model, HttpServletRequest request) throws CustomerNotFoundException {
		
		LOGGER.info("AddressController | showAddressBook is called");
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		LOGGER.info("AddressController | showAddressBook | customer : " + customer.toString());
		
		List<Address> listAddresses = addressService.listAddressBook(customer);
		
		LOGGER.info("AddressController | showAddressBook | listAddresses : " + listAddresses.toString());

		boolean usePrimaryAddressAsDefault = true;
		
		LOGGER.info("AddressController | showAddressBook | usePrimaryAddressAsDefault : " + usePrimaryAddressAsDefault);
		
		for (Address address : listAddresses) {
			
			LOGGER.info("AddressController | showAddressBook | address.isDefaultForShipping() : " + address.isDefaultForShipping());
			
			if (address.isDefaultForShipping()) {
				usePrimaryAddressAsDefault = false;
				break;
			}
		}
		
		LOGGER.info("AddressController | showAddressBook | usePrimaryAddressAsDefault : " + usePrimaryAddressAsDefault);

		model.addAttribute("listAddresses", listAddresses);
		model.addAttribute("customer", customer);
		model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);

		return "address_book/addresses";
	}
	
	@GetMapping("/address_book/new")
	public String newAddress(Model model) {
		
		LOGGER.info("AddressController | newAddress is called");
		
		List<Country> listCountries = customerService.listAllCountries();

		model.addAttribute("listCountries", listCountries);
		model.addAttribute("address", new Address());
		model.addAttribute("pageTitle", "Add New Address");
		
		LOGGER.info("AddressController | newAddress | listCountries : " + listCountries.toString());

		return "address_book/address_form";
	}

	@PostMapping("/address_book/save")
	public String saveAddress(Address address, HttpServletRequest request, RedirectAttributes ra) throws CustomerNotFoundException {
		
		LOGGER.info("AddressController | saveAddress is called");
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		LOGGER.info("AddressController | saveAddress | customer : " + customer.toString());
		LOGGER.info("AddressController | saveAddress | address : " + address.toString());
		
		address.setCustomer(customer);
		addressService.save(address);
		
		LOGGER.info("AddressController | saveAddress | message : " + "The address has been saved successfully.");

		ra.addFlashAttribute("message", "The address has been saved successfully.");
		
		String redirectOption = request.getParameter("redirect");
		String redirectURL = "redirect:/address_book";
		
		LOGGER.info("AddressController | saveAddress | redirectOption : " + redirectOption);
		LOGGER.info("AddressController | saveAddress | redirectURL : " + redirectURL);

		if ("checkout".equals(redirectOption)) {
			redirectURL += "?redirect=checkout";
		}
		
		LOGGER.info("AddressController | saveAddress | redirectURL : " + redirectURL);

		return redirectURL;
		
		
	}

	@GetMapping("/address_book/edit/{id}")
	public String editAddress(@PathVariable("id") Integer addressId, Model model,
			HttpServletRequest request) throws CustomerNotFoundException {
		
		LOGGER.info("AddressController | editAddress is called");
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		List<Country> listCountries = customerService.listAllCountries();
		
		LOGGER.info("AddressController | editAddress | customer : " + customer.toString());
		LOGGER.info("AddressController | editAddress | listCountries : " + listCountries.toString());

		Address address = addressService.get(addressId, customer.getId());
		
		LOGGER.info("AddressController | editAddress | address : " + address.toString());

		model.addAttribute("address", address);
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("pageTitle", "Edit Address (ID: " + addressId + ")");

		return "address_book/address_form";
	}

	@GetMapping("/address_book/delete/{id}")
	public String deleteAddress(@PathVariable("id") Integer addressId, RedirectAttributes ra,
			HttpServletRequest request) throws CustomerNotFoundException {
		
		LOGGER.info("AddressController | deleteAddress is called");
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		LOGGER.info("AddressController | deleteAddress | customer : " + customer.toString());
		
		addressService.delete(addressId, customer.getId());
		
		LOGGER.info("AddressController | saveAddress | message : " + "The address ID " + addressId + " has been deleted.");
		
		ra.addFlashAttribute("message", "The address ID " + addressId + " has been deleted.");

		return "redirect:/address_book";
	}
	
	@GetMapping("/address_book/default/{id}")
	public String setDefaultAddress(@PathVariable("id") Integer addressId,
			HttpServletRequest request) throws CustomerNotFoundException {
		
		LOGGER.info("AddressController | setDefaultAddress is called");
		
		Customer customer  = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		LOGGER.info("AddressController | setDefaultAddress | customer : " + customer.toString());
		
		addressService.setDefaultAddress(addressId, customer.getId());

		String redirectOption = request.getParameter("redirect");
		String redirectURL = "redirect:/address_book";
		
		LOGGER.info("AddressController | setDefaultAddress | redirectOption : " + redirectOption);
		LOGGER.info("AddressController | setDefaultAddress | redirectURL : " + redirectURL);

		if ("cart".equals(redirectOption)) {
			redirectURL = "redirect:/cart";
		}else if ("checkout".equals(redirectOption)) {
			redirectURL = "redirect:/checkout";
		}

		
		LOGGER.info("AddressController | setDefaultAddress | redirectURL : " + redirectURL);

		return redirectURL; 
	}
}
