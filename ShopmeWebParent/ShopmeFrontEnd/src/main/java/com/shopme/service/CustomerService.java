package com.shopme.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.repository.CountryRepository;
import com.shopme.repository.CustomerRepository;
import com.shopme.service.impl.ICustomerService;
import com.shopme.util.CustomerRegisterUtil;

import net.bytebuddy.utility.RandomString;

@Service
public class CustomerService implements ICustomerService{

	@Autowired 
	private CountryRepository countryRepo;
	
	@Autowired 
	private CustomerRepository customerRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}

	@Override
	public boolean isEmailUnique(String email) {
		Customer customer = customerRepo.findByEmail(email);
		return customer == null;
	}

	@Override
	public void registerCustomer(Customer customer) {
		CustomerRegisterUtil.encodePassword(customer, passwordEncoder);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());

		String randomCode = RandomString.make(64);
		customer.setVerificationCode(randomCode);

		customerRepo.save(customer);
		
	}
}
