package com.shopme.admin.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.repository.SectionRepository;
import com.shopme.common.entity.section.Section;
import com.shopme.common.exception.SectionNotFoundException;

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
	
	public Section getSection(Integer id) throws SectionNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new SectionNotFoundException("Could not find any section with ID " + id);
		}
	}	

	public void deleteSection(Integer id) throws SectionNotFoundException {
		if (!repo.existsById(id)) {
			throw new SectionNotFoundException("Could not find any section with ID " + id);
		}

		repo.deleteById(id);
	}

	public void updateSectionEnabledStatus(Integer id, boolean enabled) 
			throws SectionNotFoundException {
		if (!repo.existsById(id)) {
			throw new SectionNotFoundException("Could not find any section with ID " + id);
		}

		repo.updateEnabledStatus(id, enabled);
	}

}
