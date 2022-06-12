package com.shopme.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.shopme.admin.repository.MenuRepository;
import com.shopme.admin.service.impl.IMenuService;
import com.shopme.common.entity.menu.Menu;

public class MenuService implements IMenuService{
	
	@Autowired 
	private MenuRepository repo;

	@Override
	public List<Menu> listAll() {
		// TODO Auto-generated method stub
		return repo.findAllByOrderByTypeAscPositionAsc();
	}

}
