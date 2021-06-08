package com.shopme.admin.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.error.ProductNotFoundException;
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
	
	@Override
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Product productByName = repo.findByName(name);

		if (isCreatingNew) {
			if (productByName != null) return "Duplicate";
		} else {
			if (productByName != null && productByName.getId() != id) {
				return "Duplicate";
			}
		}

		return "OK";
	}
	
	@Override
	public void updateProductEnabledStatus(Integer id, boolean enabled) {
		repo.updateEnabledStatus(id, enabled);
	}
	
	@Override
	public void delete(Integer id) throws ProductNotFoundException {
		Long countById = repo.countById(id);

		if (countById == null || countById == 0) {
			throw new ProductNotFoundException("Could not find any product with ID " + id);			
		}

		repo.deleteById(id);
	}
	
	@Override
	public Product get(Integer id) throws ProductNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ProductNotFoundException("Could not find any product with ID " + id);
		}
	}
}
