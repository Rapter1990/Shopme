package com.shopme.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.section.Section;

public interface SectionRepository extends JpaRepository<Section, Integer> {

	@Query("SELECT NEW Section(s.id, s.heading, s.type, s.sectionOrder, s.enabled) "
			+ "FROM Section s ORDER by s.sectionOrder ASC")
	public List<Section> findAllSectionsSortedByOrder();
}
