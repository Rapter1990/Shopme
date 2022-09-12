package com.shopme.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.section.Section;

public interface SectionRepository extends JpaRepository<Section, Integer> {

	@Query("SELECT NEW Section(s.id, s.heading, s.type, s.sectionOrder, s.enabled) "
			+ "FROM Section s ORDER by s.sectionOrder ASC")
	public List<Section> findAllSectionsSortedByOrder();
	
	@Query("UPDATE Section s SET s.enabled = ?2 WHERE s.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	@Query("SELECT NEW Section(s.id) FROM Section s WHERE s.id = ?1")
	public Section getSimpleSectionById(Integer id);

	@Query("SELECT NEW Section(s.id) FROM Section s ORDER BY s.sectionOrder ASC")
	public List<Section> getOnlySectionIDsSortedByOrder();

	@Query("UPDATE Section s SET s.sectionOrder = ?1 WHERE s.id = ?2")
	@Modifying
	public void updateSectionPosition(int sectionOrder, Integer id);
}
