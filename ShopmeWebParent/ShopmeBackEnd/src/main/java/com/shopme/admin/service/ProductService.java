package com.shopme.admin.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.repository.ProductRepository;
import com.shopme.admin.service.impl.IProductService;
import com.shopme.common.entity.Product;

@Service
@Transactional
public class ProductService implements IProductService{

	@Autowired 
	private ProductRepository repo;
	
	@Override
	public List<Product> listAll() {
		return (List<Product>) repo.findAll();
	}

	@Override
	public Product save(Product product) {
		// TODO Auto-generated method stub
		if (product.getId() == null) {
			product.setCreatedTime(new Date());
		}

		if (product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replaceAll(" ", "-");
			product.setAlias(defaultAlias);
		} else {
			product.setAlias(product.getAlias().replaceAll(" ", "-"));
		}

		product.setUpdatedTime(new Date());

		return repo.save(product);
	}
}
