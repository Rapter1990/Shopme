package com.shopme.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.service.CustomerService;

@Component
public class AuthenticationControllerHelperUtil {

	@Autowired 
	private CustomerService customerService;

	public Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = CustomerAccountUtil.getEmailOfAuthenticatedCustomer(request);
		return customerService.getCustomerByEmail(email);	
	}
}
