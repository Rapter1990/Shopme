package com.shopme.admin.service.impl;

import java.util.List;

import com.shopme.common.entity.Product;

public interface IProductService {

	public List<Product> listAll();
	
	public Product save(Product product);
	
	public String checkUnique(Integer id, String name);
}
