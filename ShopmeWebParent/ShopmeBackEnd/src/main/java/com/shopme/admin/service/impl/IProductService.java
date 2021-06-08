package com.shopme.admin.service.impl;

import java.util.List;

import com.shopme.admin.error.ProductNotFoundException;
import com.shopme.common.entity.Product;

public interface IProductService {

	public List<Product> listAll();
	
	public Product save(Product product);
	
	public String checkUnique(Integer id, String name);
	
	public void updateProductEnabledStatus(Integer id, boolean enabled);
	
	public void delete(Integer id) throws ProductNotFoundException;
	
	public Product get(Integer id) throws ProductNotFoundException;
}
