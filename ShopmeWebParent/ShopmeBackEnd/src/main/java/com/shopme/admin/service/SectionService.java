package com.shopme.admin.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.repository.SectionRepository;
import com.shopme.common.entity.section.Section;

@Service
@Transactional
public class SectionService {

	@Autowired 
	private SectionRepository repo;

	public List<Section> listSections() {
		return repo.findAllSectionsSortedByOrder();
	}
	
	public void saveSection(Section section) {
		if (section.getId() == null) {
			setPositionForNewSection(section);
		}
		repo.save(section);
	}	

	private void setPositionForNewSection(Section section) {
		Long newPosition = repo.count() + 1;
		section.setSectionOrder(newPosition.intValue());
	}

}
