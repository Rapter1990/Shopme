package com.shopme.admin.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.repository.SectionRepository;
import com.shopme.common.entity.section.Section;
import com.shopme.common.exception.SectionNotFoundException;
import com.shopme.common.exception.SectionUnmoveableException;

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
	
	public void moveSectionUp(Integer id) throws SectionNotFoundException, SectionUnmoveableException {
		Section currentSection = repo.getSimpleSectionById(id);
		if (currentSection == null) {
			throw new SectionNotFoundException("Could not find any section with ID " + id);
		}

		List<Section> allSections = repo.getOnlySectionIDsSortedByOrder();

		int currentSectionIndex = allSections.indexOf(currentSection);
		if (currentSectionIndex == 0) {
			throw new SectionUnmoveableException("The section ID " + id + " is already in the first position");
		}

		int previousSectionIndex = currentSectionIndex - 1;
		Section previousSection = allSections.get(previousSectionIndex);

		currentSection.setSectionOrder(previousSectionIndex + 1);		
		previousSection.setSectionOrder(currentSectionIndex + 1);

		repo.updateSectionPosition(currentSection.getSectionOrder(), currentSection.getId());
		repo.updateSectionPosition(previousSection.getSectionOrder(), previousSection.getId());
	}

	public void moveSectionDown(Integer id) throws SectionNotFoundException, SectionUnmoveableException {
		Section currentSection = repo.getSimpleSectionById(id);
		if (currentSection == null) {
			throw new SectionNotFoundException("Could not find any section with ID " + id);
		}

		List<Section> allSections = repo.getOnlySectionIDsSortedByOrder();

		int currentSectionIndex = allSections.indexOf(currentSection);
		if (currentSectionIndex == allSections.size() - 1) {
			throw new SectionUnmoveableException("The section ID " + id + " is already in the last position");
		}

		int nextSectionIndex = currentSectionIndex + 1;
		Section nextSection = allSections.get(nextSectionIndex);

		currentSection.setSectionOrder(nextSectionIndex + 1);		
		nextSection.setSectionOrder(currentSectionIndex + 1);

		repo.updateSectionPosition(currentSection.getSectionOrder(), currentSection.getId());
		repo.updateSectionPosition(nextSection.getSectionOrder(), nextSection.getId());
	}

}
