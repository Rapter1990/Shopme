package com.shopme.admin.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.repository.MenuRepository;
import com.shopme.common.entity.article.Article;
import com.shopme.common.entity.menu.Menu;
import com.shopme.common.entity.menu.MenuType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class MenuRepositoryTests {

	@Autowired 
	private MenuRepository repo;
	
	@Test
	public void testCreateHeaderMenu() {
		Menu menu = new Menu();
		menu.setType(MenuType.HEADER);
		menu.setTitle("About Shopme");
		menu.setAlias("about");
		menu.setEnabled(true);
		menu.setPosition(1);

		menu.setArticle(new Article(1));

		Menu savedMenu = repo.save(menu);

		assertTrue(savedMenu.getId() > 0);
	}

	@Test
	public void testCreateFooterMenu() {
		Menu menu = new Menu();
		menu.setType(MenuType.FOOTER);
		menu.setTitle("Shipping");
		menu.setAlias("shipping");
		menu.setEnabled(false);
		menu.setPosition(2);

		menu.setArticle(new Article(4));

		Menu savedMenu = repo.save(menu);

		assertTrue(savedMenu.getId() > 0);
	}	
	
	@Test
	public void testListMenuByTypeThenByPosition() {
		List<Menu> listMenuItems = repo.findAllByOrderByTypeAscPositionAsc();
		assertThat(listMenuItems.size()).isGreaterThan(0);

		listMenuItems.forEach(System.out::println);
	}
	
	@Test
	public void testCountHeaderMenus() {
		Long numberOfFooterMenus = repo.countByType(MenuType.HEADER);
		assertEquals(1, numberOfFooterMenus);
	}
	
	@Test
	public void testCountFooterMenus() {
		Long numberOfFooterMenus = repo.countByType(MenuType.FOOTER);
		assertEquals(1, numberOfFooterMenus);
	}	

	@Test
	public void testDisableMenuItem() {
		Integer menuId = 1;
		repo.updateEnabledStatus(menuId, false);
		Menu updatedMenu = repo.findById(menuId).get();

		assertFalse(updatedMenu.isEnabled());
	}

	@Test
	public void testEnableMenuItem() {
		Integer menuId = 1;
		repo.updateEnabledStatus(menuId, true);
		Menu updatedMenu = repo.findById(menuId).get();

		assertTrue(updatedMenu.isEnabled());
	}
	
	@Test
	public void testListHeaderMenuItems() {
		List<Menu> listHeaderMenuItems = repo.findByTypeOrderByPositionAsc(MenuType.HEADER);
		assertThat(listHeaderMenuItems).isNotEmpty();

		listHeaderMenuItems.forEach(System.out::println);
	}

	@Test
	public void testListFooterMenuItems() {
		List<Menu> listFooterMenuItems = repo.findByTypeOrderByPositionAsc(MenuType.FOOTER);
		assertThat(listFooterMenuItems).isNotEmpty();

		listFooterMenuItems.forEach(System.out::println);
	}

}
