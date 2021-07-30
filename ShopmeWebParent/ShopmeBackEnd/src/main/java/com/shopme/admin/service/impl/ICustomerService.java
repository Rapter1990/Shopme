package com.shopme.admin.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;

import com.shopme.admin.error.CustomerNotFoundException;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

public interface ICustomerService {

	public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword);
	public void updateCustomerEnabledStatus(Integer id, boolean enabled);
	public List<Country> listAllCountries();
	public boolean isEmailUnique(Integer id, String email);
	public void save(Customer customerInForm);
	public void delete(Integer id) throws CustomerNotFoundException;
	public Customer get(Integer id) throws CustomerNotFoundException;
	List<Customer> listAll();
}
