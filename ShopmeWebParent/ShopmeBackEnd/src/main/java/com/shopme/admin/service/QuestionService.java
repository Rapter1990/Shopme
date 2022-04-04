package com.shopme.admin.service;


import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.repository.QuestionRepository;
import com.shopme.admin.service.impl.IQuestionService;

@Service
@Transactional
public class QuestionService implements IQuestionService{

	public static final int QUESTIONS_PER_PAGE = 10;
	
	private final QuestionRepository repo;
	
	public QuestionService(QuestionRepository repo) {
		super();
		this.repo = repo;
	}

	@Override
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		// TODO Auto-generated method stub
		helper.listEntities(pageNum, QUESTIONS_PER_PAGE, repo);
	}

}
