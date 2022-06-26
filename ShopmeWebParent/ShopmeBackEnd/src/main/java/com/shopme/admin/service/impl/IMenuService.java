package com.shopme.admin.service.impl;

import java.util.List;

import com.shopme.admin.error.MenuUnmoveableException;
import com.shopme.admin.util.MenuMoveDirection;
import com.shopme.common.entity.menu.Menu;
import com.shopme.common.exception.MenuItemNotFoundException;

public interface IMenuService{

	public List<Menu> listAll();
	public void save(Menu menu);
	public Menu get(Integer id) throws MenuItemNotFoundException;
	public void updateEnabledStatus(Integer id, boolean enabled) throws MenuItemNotFoundException;
	public void delete(Integer id) throws MenuItemNotFoundException;
	public void moveMenu(Integer id,MenuMoveDirection direction) throws MenuUnmoveableException, MenuItemNotFoundException;
}
