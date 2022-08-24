package com.shopme.admin.section;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.repository.SectionRepository;
import com.shopme.common.entity.section.Section;
import com.shopme.common.entity.section.SectionType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SectionRepositoryTests {

	@Autowired 
	private SectionRepository repo;

	@Test
	public void testAddTextSection() {
		Section section = new Section();
		section.setHeading("Annoucement");
		section.setDescription("Shop Annoucement in Summer 2022: The great sales season is coming...");
		section.setType(SectionType.TEXT);
		section.setSectionOrder(1);

		Section savedSection = repo.save(section);

		assertThat(savedSection).isNotNull();
		assertThat(savedSection.getId()).isGreaterThan(0);
	}

	@Test
	public void testListSectionsNotSorted() {
		List<Section> sections = repo.findAll();
		assertThat(sections).isNotEmpty();

		sections.forEach(System.out::println);
	}
	
	@Test
	public void testListSectionsSorted() {
		List<Section> sections = repo.findAllSectionsSortedByOrder();

		assertThat(sections).isNotEmpty();		
		sections.forEach(System.out::println);		
	}

	@Test
	public void testAddAllCategoriesSection() {
		Section section = new Section();
		section.setHeading("Shopping by Categories");
		section.setDescription("Check out all categories...");
		section.setType(SectionType.ALL_CATEGORIES);
		section.setSectionOrder(2);

		Section savedSection = repo.save(section);

		assertThat(savedSection).isNotNull();
		assertThat(savedSection.getId()).isGreaterThan(0);		
	}
	
	@Test
	public void testDisableSection() {
		Integer sectionId = 7;
		repo.updateEnabledStatus(sectionId, false);
		Section section = repo.findById(sectionId).get();

		assertThat(section.isEnabled()).isFalse();
	}

	@Test
	public void testEnableSection() {
		Integer sectionId = 7;
		repo.updateEnabledStatus(sectionId, true);
		Section section = repo.findById(sectionId).get();

		assertThat(section.isEnabled()).isTrue();
	}
	
	@Test
	public void testDeleteSection() {
		Integer sectionId = 3;
		repo.deleteById(sectionId);
		Optional<Section> findById = repo.findById(sectionId);

		assertThat(findById).isNotPresent();
	}
}
