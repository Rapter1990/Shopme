package com.shopme.admin.paging;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PagingAndSortingHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(PagingAndSortingHelper.class);
	
	private ModelAndViewContainer model;
	private String listName;
	private String sortField;
	private String sortDir;
	private String keyword;
	
	public PagingAndSortingHelper(ModelAndViewContainer model,String listName,
			String sortField, String sortDir, String keyword) {
		this.model = model;
		this.listName = listName;
		this.sortField = sortField;
		this.sortDir = sortDir;
		this.keyword = keyword;
	}

	public void updateModelAttributes(int pageNum, Page<?> page) {
		
		LOGGER.info("PagingAndSortingHelper | updateModelAttributes is started");
		
		List<?> listItems = page.getContent();
		int pageSize = page.getSize();

		long startCount = (pageNum - 1) * pageSize + 1;
		long endCount = startCount + pageSize - 1;
		
		LOGGER.info("PagingAndSortingHelper | updateModelAttributes | startCount : " + startCount);
		LOGGER.info("PagingAndSortingHelper | updateModelAttributes | endCount : " + endCount);
		LOGGER.info("PagingAndSortingHelper | updateModelAttributes | page.getTotalElements() : " + page.getTotalElements());
		
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		LOGGER.info("PagingAndSortingHelper | updateModelAttributes | endCount : " + endCount);

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute(listName, listItems);
		
		LOGGER.info("PagingAndSortingHelper | updateModelAttributes | currentPage : " + pageNum);
		LOGGER.info("PagingAndSortingHelper | updateModelAttributes | totalPages : " + page.getTotalPages());
		LOGGER.info("PagingAndSortingHelper | updateModelAttributes | totalItems : " + page.getTotalElements());
		LOGGER.info("PagingAndSortingHelper | updateModelAttributes | listName : " + listItems);
		
		
	}

	public void listEntities(int pageNum, int pageSize, SearchRepository<?, Integer> repo) {
		
		LOGGER.info("PagingAndSortingHelper | listEntities is started");
		
		Pageable pageable = createPageable(pageSize, pageNum);
		Page<?> page = null;

		if (keyword != null) {
			page = repo.findAll(keyword, pageable);
		} else {
			page = repo.findAll(pageable);
		}
		
		LOGGER.info("PagingAndSortingHelper | listEntities | page : " + page.toString());
		LOGGER.info("PagingAndSortingHelper | listEntities | pageNum : " + pageNum);
		

		updateModelAttributes(pageNum, page);		
	}

	public Pageable createPageable(int pageSize, int pageNum) {
		
		LOGGER.info("PagingAndSortingHelper | createPageable is started");
		
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		LOGGER.info("PagingAndSortingHelper | createPageable | sort : " + sort.toString());
		LOGGER.info("PagingAndSortingHelper | createPageable | pageNum - 1 : " + (pageNum - 1));
		LOGGER.info("PagingAndSortingHelper | createPageable | pageSize : " + pageSize);
		
		return PageRequest.of(pageNum - 1, pageSize, sort);		
	}	

	public String getSortField() {
		return sortField;
	}

	public String getSortDir() {
		return sortDir;
	}

	public String getKeyword() {
		return keyword;
	}
}
