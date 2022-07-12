package com.shopme.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopme.common.entity.menu.Menu;
import com.shopme.common.entity.menu.MenuType;
import com.shopme.repository.MenuRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class MenuRepositoryTests {

	@Autowired 
	private MenuRepository repo;

	@Test
	public void testListHeaderMenuItems() {
		List<Menu> listHeaderMenuItems = repo.findByTypeAndEnabledOrderByPositionAsc(MenuType.HEADER, true);
		assertThat(listHeaderMenuItems).isNotEmpty();

		listHeaderMenuItems.forEach(System.out::println);
	}

	@Test
	public void testListFooterMenuItems() {
		List<Menu> listFooterMenuItems = repo.findByTypeAndEnabledOrderByPositionAsc(MenuType.FOOTER, true);
		assertThat(listFooterMenuItems).isNotEmpty();

		listFooterMenuItems.forEach(System.out::println);
	}
	
	@Test
	public void testFindMenuByAliasNotFound() {
		String alias = "test-alias";
		Menu menu = repo.findByAlias(alias);
		assertThat(menu).isNull();		
	}

	@Test
	public void testFindMenuByAliasFound() {
		String alias = "privacy-policy";
		Menu menu = repo.findByAlias(alias);
		assertThat(menu).isNotNull();		
	}
}
