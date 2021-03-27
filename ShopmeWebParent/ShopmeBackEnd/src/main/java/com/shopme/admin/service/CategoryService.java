package com.shopme.admin.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.repository.CategoryRepository;
import com.shopme.admin.service.impl.ICategoryService;
import com.shopme.common.entity.Category;

@Service
@Transactional
public class CategoryService implements ICategoryService{
	
	@Autowired
	private CategoryRepository categoryRepository;  

	@Override
	public List<Category> listAll() {
		// TODO Auto-generated method stub
		List<Category> categoryList = new ArrayList<>();
		categoryRepository.findAll().forEach(categoryList::add);
		return categoryList;
	}

}
