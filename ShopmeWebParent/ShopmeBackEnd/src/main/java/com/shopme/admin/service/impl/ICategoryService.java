package com.shopme.admin.service.impl;

import java.util.List;

import com.shopme.admin.util.CategoryPageInfo;
import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;

public interface ICategoryService {

	public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum, String sortDir,
			String keyword);
	
	public List<Category> listCategoriesUsedInForm();
	
	public Category save(Category category);
	
	public Category get(Integer id) throws CategoryNotFoundException;
	
	public String checkUnique(Integer id, String name, String alias);
	
	public void updateCategoryEnabledStatus(Integer id, boolean enabled);
	
	public void delete(Integer id) throws CategoryNotFoundException;
}
