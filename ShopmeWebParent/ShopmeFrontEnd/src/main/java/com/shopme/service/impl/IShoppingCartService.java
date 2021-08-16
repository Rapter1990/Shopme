package com.shopme.service.impl;

import java.util.List;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ShoppingCartException;

public interface IShoppingCartService {

	public Integer addProduct(Integer productId, Integer quantity, Customer customer) 
			throws ShoppingCartException;
	
	public List<CartItem> listCartItems(Customer customer);
}
