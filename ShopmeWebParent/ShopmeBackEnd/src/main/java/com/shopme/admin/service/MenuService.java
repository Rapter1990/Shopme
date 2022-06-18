package com.shopme.admin.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;

import com.shopme.admin.repository.MenuRepository;
import com.shopme.admin.service.impl.IMenuService;
import com.shopme.common.entity.menu.Menu;
import com.shopme.common.exception.MenuItemNotFoundException;

public class MenuService implements IMenuService{
	
	@Autowired 
	private MenuRepository repo;

	@Override
	public List<Menu> listAll() {
		// TODO Auto-generated method stub
		return repo.findAllByOrderByTypeAscPositionAsc();
	}

	@Override
	public void save(Menu menu) {
		// TODO Auto-generated method stub
		if (menu.getAlias() == null || menu.getAlias().isEmpty()) {
			menu.setAlias(menu.getTitle().replaceAll(" ", "-"));
		}
		
		if (menu.getId() == null) {
			Long newPosition = repo.countByType(menu.getType()) + 1;
			menu.setPosition(newPosition.intValue());
		}
		
		repo.save(menu);
	}

	@Override
	public Menu get(Integer id) throws MenuItemNotFoundException {
		// TODO Auto-generated method stub
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new MenuItemNotFoundException("Could not find any menu item with ID " + id);
		}
	}

	@Override
	public void updateEnabledStatus(Integer id, boolean enabled) throws MenuItemNotFoundException {
		// TODO Auto-generated method stub
		if (!repo.existsById(id)) {
			throw new MenuItemNotFoundException("Could not find any menu item with ID " + id);
		}
		repo.updateEnabledStatus(id, enabled);
	}

	@Override
	public void delete(Integer id) throws MenuItemNotFoundException {
		// TODO Auto-generated method stub
		if (!repo.existsById(id)) {
			throw new MenuItemNotFoundException("Could not find any menu item with ID " + id);
		}
		repo.deleteById(id);
	}

}
