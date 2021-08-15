package com.shopme.service.impl;

import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ShoppingCartException;

public interface IShoppingCartService {

	public Integer addProduct(Integer productId, Integer quantity, Customer customer) 
			throws ShoppingCartException;
}
