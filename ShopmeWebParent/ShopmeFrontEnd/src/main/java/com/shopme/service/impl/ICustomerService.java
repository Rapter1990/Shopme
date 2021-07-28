package com.shopme.service.impl;

import java.util.List;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

public interface ICustomerService {

	public List<Country> listAllCountries();
	public boolean isEmailUnique(String email);
	public void registerCustomer(Customer customer);
}
