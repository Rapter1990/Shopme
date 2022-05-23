package com.shopme.admin.service.impl;

import java.util.List;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.User;
import com.shopme.common.entity.article.Article;
import com.shopme.common.entity.article.ArticleType;
import com.shopme.common.exception.ArticleNotFoundException;

public interface IArticleService {
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper);
	public Article get(Integer id) throws ArticleNotFoundException;
	public void save(Article article, User user);
	public void delete(Integer id) throws ArticleNotFoundException;
	public void updatePublishStatus(Integer id, boolean published) throws ArticleNotFoundException;
	public List<Article> findByTypeOrderByTitle(ArticleType type);
}
