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
		
		LOGGER.info("PagingAndSortingArgumentResolver | updateModelAttributes is started");
		
		List<?> listItems = page.getContent();
		int pageSize = page.getSize();

		long startCount = (pageNum - 1) * pageSize + 1;
		long endCount = startCount + pageSize - 1;
		
		LOGGER.info("PagingAndSortingArgumentResolver | updateModelAttributes | startCount : " + startCount);
		LOGGER.info("PagingAndSortingArgumentResolver | updateModelAttributes | endCount : " + endCount);
		LOGGER.info("PagingAndSortingArgumentResolver | updateModelAttributes | page.getTotalElements() : " + page.getTotalElements());
		
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		LOGGER.info("PagingAndSortingArgumentResolver | updateModelAttributes | endCount : " + endCount);

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute(listName, listItems);
		
		LOGGER.info("PagingAndSortingArgumentResolver | updateModelAttributes | currentPage : " + pageNum);
		LOGGER.info("PagingAndSortingArgumentResolver | updateModelAttributes | totalPages : " + page.getTotalPages());
		LOGGER.info("PagingAndSortingArgumentResolver | updateModelAttributes | totalItems : " + page.getTotalElements());
		LOGGER.info("PagingAndSortingArgumentResolver | updateModelAttributes | listName : " + listItems);
		
		
	}

	public void listEntities(int pageNum, int pageSize, SearchRepository<?, Integer> repo) {
		
		LOGGER.info("PagingAndSortingArgumentResolver | listEntities is started");
		
		Pageable pageable = createPageable(pageSize, pageNum);
		Page<?> page = null;

		if (keyword != null) {
			page = repo.findAll(keyword, pageable);
		} else {
			page = repo.findAll(pageable);
		}
		
		LOGGER.info("PagingAndSortingArgumentResolver | listEntities | page : " + page.toString());
		LOGGER.info("PagingAndSortingArgumentResolver | listEntities | pageNum : " + pageNum);
		

		updateModelAttributes(pageNum, page);		
	}

	public Pageable createPageable(int pageSize, int pageNum) {
		
		LOGGER.info("PagingAndSortingArgumentResolver | createPageable is started");
		
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		LOGGER.info("PagingAndSortingArgumentResolver | createPageable | sort : " + sort.toString());
		LOGGER.info("PagingAndSortingArgumentResolver | createPageable | pageNum - 1 : " + (pageNum - 1));
		LOGGER.info("PagingAndSortingArgumentResolver | createPageable | pageSize : " + pageSize);
		
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
