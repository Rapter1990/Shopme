package com.shopme.service.impl;

import java.util.List;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;

public interface ICustomerService {

	public List<Country> listAllCountries();
	public boolean isEmailUnique(String email);
	public void registerCustomer(Customer customer);
	public boolean verify(String verificationCode);
	public Customer getCustomerByEmail(String email);
	public void updateAuthenticationType(Customer customer, AuthenticationType type);
	public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode,
			AuthenticationType authenticationType);
	public void update(Customer customerInForm);
	public String updateResetPasswordToken(String email) throws CustomerNotFoundException;
	public Customer getByResetPasswordToken(String token);
	public void updatePassword(String token, String newPassword) throws CustomerNotFoundException;
}
