package com.shopme.util;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.service.CustomerService;

public class CustomerShoppingCartUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerShoppingCartUtil.class);
	
	public static Customer getAuthenticatedCustomer(HttpServletRequest request, CustomerService customerService) 
			throws CustomerNotFoundException {
		
		LOGGER.info("CustomerShoppingCartUtil | getAuthenticatedCustomer is called");
		
		String email = CustomerAccountUtil.getEmailOfAuthenticatedCustomer(request);
		
		LOGGER.info("CustomerShoppingCartUtil | getAuthenticatedCustomer | email : " + email);
		
		if (email == null) {
			throw new CustomerNotFoundException("No authenticated customer");
		}
		
		LOGGER.info("CustomerShoppingCartUtil | getAuthenticatedCustomer | customerService.getCustomerByEmail(email) : " + customerService.getCustomerByEmail(email).toString());

		return customerService.getCustomerByEmail(email);
	}
	
}
