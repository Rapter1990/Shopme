package com.shopme.service.impl;

import java.util.List;

import com.shopme.common.entity.Country;

public interface ICustomerService {

	public List<Country> listAllCountries();
	public boolean isEmailUnique(String email);
}
