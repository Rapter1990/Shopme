package com.shopme.service.impl;

import java.util.List;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;

public interface ICategoryService {

	public List<Category> listNoChildrenCategories();
	
	public Category getCategory(String alias) throws CategoryNotFoundException;
	
	public List<Category> getCategoryParents(Category child);
}
