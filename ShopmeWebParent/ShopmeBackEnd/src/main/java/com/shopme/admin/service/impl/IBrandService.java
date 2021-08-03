package com.shopme.admin.service.impl;

import java.util.List;

import com.shopme.admin.error.BrandNotFoundException;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Brand;

public interface IBrandService {

	public List<Brand> listAll();
	public Brand save(Brand brand);
	public Brand get(Integer id) throws BrandNotFoundException;
	public void delete(Integer id) throws BrandNotFoundException;
	public String checkUnique(Integer id, String name);
	public void listByPage(int pageNum, PagingAndSortingHelper helper);
}
