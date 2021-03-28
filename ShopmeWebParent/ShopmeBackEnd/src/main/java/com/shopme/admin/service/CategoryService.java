package com.shopme.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

	@Override
	public List<Category> listCategoriesUsedInForm() {
		
		List<Category> categoriesUsedInForm = new ArrayList<>();

		Iterable<Category> categoriesInDB = categoryRepository.findAll();

		for (Category category : categoriesInDB) {
			if (category.getParent() == null) {
				categoriesUsedInForm.add(new Category(category.getName()));

				Set<Category> children = category.getChildren();

				for (Category subCategory : children) {
					String name = "--" + subCategory.getName();
					categoriesUsedInForm.add(new Category(name));

					listChildren(categoriesUsedInForm, subCategory, 1);
				}
			}
		}		

		return categoriesUsedInForm;
	}
	
	private void listChildren(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();

		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {				
				name += "--";
			}
			name += subCategory.getName();

			categoriesUsedInForm.add(new Category(name));

			listChildren(categoriesUsedInForm, subCategory, newSubLevel);
		}		
	}

}
