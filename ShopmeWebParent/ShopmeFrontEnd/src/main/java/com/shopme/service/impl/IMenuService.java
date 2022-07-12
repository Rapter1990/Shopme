package com.shopme.service.impl;

import java.util.List;

import com.shopme.common.entity.article.Article;
import com.shopme.common.entity.menu.Menu;
import com.shopme.common.exception.MenuItemNotFoundException;

public interface IMenuService {

	public List<Menu> getHeaderMenuItems();
	public List<Menu> getFooterMenuItems();
	public Article getArticleBoundToMenu(String menuAlias) throws MenuItemNotFoundException;
}
