package com.shopme.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.section.Section;
import com.shopme.repository.SectionRepository;

@Service
@Transactional
public class SectionService {
	
	@Autowired 
	private SectionRepository repo;

	public List<Section> listEnabledSections() {
		return repo.findAllByEnabledOrderBySectionOrderAsc(true);
	}
	
	
}
