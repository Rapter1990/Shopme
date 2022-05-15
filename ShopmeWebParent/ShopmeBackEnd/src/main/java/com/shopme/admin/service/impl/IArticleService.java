package com.shopme.admin.service.impl;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.User;
import com.shopme.common.entity.article.Article;
import com.shopme.common.exception.ArticleNotFoundException;

public interface IArticleService {
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper);
	public Article get(Integer id) throws ArticleNotFoundException;
	public void save(Article article, User user);
	public void delete(Integer id) throws ArticleNotFoundException;
}
