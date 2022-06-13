package com.shopme.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopme.common.entity.menu.Menu;
import com.shopme.common.entity.menu.MenuType;

public interface MenuRepository extends JpaRepository<Menu, Integer> {

	public List<Menu> findAllByOrderByTypeAscPositionAsc();
	
	public Long countByType(MenuType type);
}
