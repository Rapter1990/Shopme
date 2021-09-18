package com.shopme.service.impl;

import org.springframework.data.domain.Page;

import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;

public interface IProductService {

	public Page<Product> listByCategory(int pageNum, Integer categoryId);
	
	public Product getProduct(String alias) throws ProductNotFoundException;
	
	public Page<Product> search(String keyword, int pageNum);
}
