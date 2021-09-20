package com.shopme.service.impl;

import java.util.List;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;

public interface IAddressService {

	public List<Address> listAddressBook(Customer customer);
	
	public void save(Address address);

	public Address get(Integer addressId, Integer customerId);
	
	public void delete(Integer addressId, Integer customerId);
	
	public void setDefaultAddress(Integer defaultAddressId, Integer customerId) ;

	public Address getDefaultAddress(Customer customer);
}
