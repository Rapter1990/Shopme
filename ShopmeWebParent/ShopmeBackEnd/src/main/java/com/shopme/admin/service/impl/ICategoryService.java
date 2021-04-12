package com.shopme.admin.service.impl;

import java.util.List;

import com.shopme.admin.error.CategoryNotFoundException;
import com.shopme.common.entity.Category;

public interface ICategoryService {

	public List<Category> listAll();
	
	public List<Category> listCategoriesUsedInForm();
	
	public Category save(Category category);
	
	public Category get(Integer id) throws CategoryNotFoundException;
	
	public String checkUnique(Integer id, String name, String alias);
}
