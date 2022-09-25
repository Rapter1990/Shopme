package com.shopme.section;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopme.common.entity.section.Section;
import com.shopme.repository.SectionRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SectionRepositoryTests {
	
	@Autowired 
	private SectionRepository repo;

	@Test
	public void testListEnabledSections() {
		List<Section> sections = repo.findAllByEnabledOrderBySectionOrderAsc(true);

		sections.forEach(section -> {
			System.out.println(section);
			if (!section.isEnabled()) assert false;
		});

		assert true;
	}
}
