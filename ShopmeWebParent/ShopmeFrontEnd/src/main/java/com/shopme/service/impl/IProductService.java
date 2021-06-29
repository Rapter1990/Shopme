package com.shopme.service.impl;

import org.springframework.data.domain.Page;

import com.shopme.common.entity.Product;

public interface IProductService {

	public Page<Product> listByCategory(int pageNum, Integer categoryId);
}
