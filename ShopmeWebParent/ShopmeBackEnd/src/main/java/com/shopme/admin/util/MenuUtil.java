package com.shopme.admin.util;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shopme.admin.error.MenuUnmoveableException;
import com.shopme.admin.repository.MenuRepository;
import com.shopme.common.entity.menu.Menu;
import com.shopme.common.exception.MenuItemNotFoundException;

public class MenuUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(MenuUtil.class);
	
	public static void setDefaultAlias(Menu menu) {
		
		LOGGER.info("MenuUtil | setDefaultAlias is called");
		LOGGER.info("MenuUtil | setDefaultAlias | menu.getAlias() == null : " + (menu.getAlias() == null));
		LOGGER.info("MenuUtil | setDefaultAlias | menu.getAlias() == null : " + (menu.getAlias() == null));
		
		if (menu.getAlias() == null || menu.getAlias().isEmpty()) {
			
			LOGGER.info("MenuUtil | setDefaultAlias | menu.getTitle() : " + (menu.getTitle()));
			menu.setAlias(menu.getTitle().replaceAll(" ", "-"));
		}		
	}
	
	public static void setPositionForEditedMenu(MenuRepository repo,Menu menu) {
		
		LOGGER.info("MenuUtil | setPositionForEditedMenu is called");
		
		Menu existMenu = repo.findById(menu.getId()).get();
		
		LOGGER.info("MenuUtil | setPositionForEditedMenu | existMenu :" + existMenu.toString());

		LOGGER.info("MenuUtil | setPositionForEditedMenu | !existMenu.getType().equals(menu.getType()) : " 
				+ (!existMenu.getType().equals(menu.getType())));
		
		if (!existMenu.getType().equals(menu.getType())) {
			// if the menu type changed, then set its position at the last
			setPositionForNewMenu(repo,menu);
		}
	}
	
	public static void setPositionForNewMenu(MenuRepository repo,Menu newMenu) {
		
		LOGGER.info("MenuUtil | setPositionForNewMenu is called");
		
		// newly added menu always has position at the last
		Long newPosition = repo.countByType(newMenu.getType()) + 1;
		
		LOGGER.info("MenuUtil | setPositionForNewMenu | newPosition : " + newPosition);
		
		newMenu.setPosition(newPosition.intValue());		
	}
	
	
	public static void moveMenuUp(MenuRepository repo,Integer id) 
			throws MenuUnmoveableException, MenuItemNotFoundException {
		
		LOGGER.info("MenuUtil | moveMenuUp is called");
		
		Optional<Menu> findById = repo.findById(id);
		if (!findById.isPresent()) {
			
			LOGGER.info("MenuUtil | moveMenuUp | !findById.isPresent() error | " + "Could not find any menu item with ID " + id);
			throw new MenuItemNotFoundException("Could not find any menu item with ID " + id);
		}

		Menu currentMenu = findById.get();
		LOGGER.info("MenuUtil | moveMenuUp | currentMenu :" + currentMenu.toString());
		
		List<Menu> allMenusOfSameType = repo.findByTypeOrderByPositionAsc(currentMenu.getType());
		LOGGER.info("MenuUtil | moveMenuUp | allMenusOfSameType :" + allMenusOfSameType.toString());
		
		int currentMenuIndex = allMenusOfSameType.indexOf(currentMenu);
		LOGGER.info("MenuUtil | moveMenuUp | currentMenuIndex :" + currentMenuIndex);

		if (currentMenuIndex == 0) {
			LOGGER.info("MenuUtil | moveMenuUp | currentMenuIndex == 0 | " + "The menu ID " + id + " is already in the first position");
			throw new MenuUnmoveableException("The menu ID " + id + " is already in the first position");
		}

		// swap current menu item with the previous one, thus the given menu is moved u
		int previousMenuIndex = currentMenuIndex - 1;
		LOGGER.info("MenuUtil | moveMenuUp | previousMenuIndex :" + previousMenuIndex);
		
		Menu previousMenu = allMenusOfSameType.get(previousMenuIndex);
		LOGGER.info("MenuUtil | moveMenuUp | previousMenu :" + previousMenu.toString());

		LOGGER.info("MenuUtil | moveMenuUp | currentMenu position : " + (previousMenuIndex + 1));
		
		currentMenu.setPosition(previousMenuIndex + 1);
		allMenusOfSameType.set(previousMenuIndex, currentMenu);
		
		LOGGER.info("MenuUtil | moveMenuUp | previousMenu position : " + (currentMenuIndex + 1));

		previousMenu.setPosition(currentMenuIndex + 1);
		allMenusOfSameType.set(currentMenuIndex, previousMenu);

		// update all menu items of the same type
		repo.saveAll(Arrays.asList(currentMenu, previousMenu));
	}

	public static void moveMenuDown(MenuRepository repo,Integer id) 
			throws MenuUnmoveableException, MenuItemNotFoundException {
		
		LOGGER.info("MenuUtil | moveMenuDown is called");
		
		Optional<Menu> findById = repo.findById(id);
		if (!findById.isPresent()) {
			LOGGER.info("MenuUtil | moveMenuDown | !findById.isPresent() error | " + "Could not find any menu item with ID " + id);
			throw new MenuItemNotFoundException("Could not find any menu item with ID " + id);
		}

		Menu currentMenu = findById.get();
		LOGGER.info("MenuUtil | moveMenuDown | currentMenu :" + currentMenu.toString());
		
		List<Menu> allMenusOfSameType = repo.findByTypeOrderByPositionAsc(currentMenu.getType());
		LOGGER.info("MenuUtil | moveMenuDown | allMenusOfSameType :" + allMenusOfSameType.toString());
		
		int currentMenuIndex = allMenusOfSameType.indexOf(currentMenu);
		LOGGER.info("MenuUtil | moveMenuDown | currentMenuIndex :" + currentMenuIndex);

		LOGGER.info("MenuUtil | moveMenuDown | allMenusOfSameType.size() - 1 :" + (allMenusOfSameType.size() - 1));
		
		if (currentMenuIndex == allMenusOfSameType.size() - 1) {
			throw new MenuUnmoveableException("The menu ID " + id + " is already in the last position");
		}

		// swap current menu item with the next one, thus the given menu is moved down
		int nextMenuIndex = currentMenuIndex + 1;
		LOGGER.info("MenuUtil | moveMenuDown | nextMenuIndex :" + nextMenuIndex);
		
		Menu nextMenu = allMenusOfSameType.get(nextMenuIndex);
		LOGGER.info("MenuUtil | moveMenuDown | nextMenu :" + nextMenu.toString());

		LOGGER.info("MenuUtil | moveMenuDown | currentMenu position : " + (nextMenuIndex + 1));
		
		currentMenu.setPosition(nextMenuIndex + 1);
		allMenusOfSameType.set(nextMenuIndex, currentMenu);

		LOGGER.info("MenuUtil | moveMenuDown | nextMenu position : " + (currentMenuIndex + 1));
		
		nextMenu.setPosition(currentMenuIndex + 1);
		allMenusOfSameType.set(currentMenuIndex, nextMenu);

		// update all menu items of the same type
		repo.saveAll(Arrays.asList(currentMenu, nextMenu));
	}
}
