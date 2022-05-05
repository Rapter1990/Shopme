package com.shopme.admin.service.impl;

import com.shopme.admin.paging.PagingAndSortingHelper;

public interface IArticleService {
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper);
}
