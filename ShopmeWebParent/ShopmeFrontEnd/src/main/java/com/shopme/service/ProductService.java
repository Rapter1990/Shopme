package com.shopme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;
import com.shopme.repository.ProductRepository;
import com.shopme.service.impl.IProductService;

@Service
public class ProductService implements IProductService{

	public static final int PRODUCTS_PER_PAGE = 10;

	@Autowired 
	private ProductRepository repo;
	
	@Override
	public Page<Product> listByCategory(int pageNum, Integer categoryId) {
		// TODO Auto-generated method stub
		String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);

		return repo.listByCategory(categoryId, categoryIdMatch, pageable);
	}

}
