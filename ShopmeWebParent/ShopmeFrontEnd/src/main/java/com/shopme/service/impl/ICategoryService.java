package com.shopme.service.impl;

import java.util.List;

import com.shopme.common.entity.Category;

public interface ICategoryService {

	public List<Category> listNoChildrenCategories();
	
	public Category getCategory(String alias);
	
	public List<Category> getCategoryParents(Category child);
}
