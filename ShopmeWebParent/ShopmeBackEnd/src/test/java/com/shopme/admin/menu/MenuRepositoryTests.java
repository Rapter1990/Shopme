package com.shopme.admin.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.repository.MenuRepository;
import com.shopme.common.entity.menu.Menu;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class MenuRepositoryTests {

	@Autowired 
	private MenuRepository repo;
	
	@Test
	public void testListMenuByTypeThenByPosition() {
		List<Menu> listMenuItems = repo.findAllByOrderByTypeAscPositionAsc();
		assertThat(listMenuItems.size()).isGreaterThan(0);

		listMenuItems.forEach(System.out::println);
	}
}
