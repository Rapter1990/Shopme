package com.shopme.admin.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;

import com.shopme.common.entity.Product;
import com.shopme.common.error.ProductNotFoundException;

public interface IProductService {

	public List<Product> listAll();
	
	public Product save(Product product);
	
	public String checkUnique(Integer id, String name);
	
	public void updateProductEnabledStatus(Integer id, boolean enabled);
	
	public void delete(Integer id) throws ProductNotFoundException;
	
	public Product get(Integer id) throws ProductNotFoundException;
	
	public Page<Product> listByPage(int pageNum, String sortField, String sortDir, 
			String keyword, Integer categoryId);
	
	public void saveProductPrice(Product productInForm);
	
}
