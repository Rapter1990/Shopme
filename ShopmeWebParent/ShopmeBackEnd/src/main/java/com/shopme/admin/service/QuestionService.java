package com.shopme.admin.service;


import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.repository.QuestionRepository;
import com.shopme.admin.service.impl.IQuestionService;
import com.shopme.common.entity.question.Question;
import com.shopme.common.exception.QuestionNotFoundException;

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

	@Override
	public Question getQuestionById(Integer id) throws QuestionNotFoundException {
		// TODO Auto-generated method stub
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new QuestionNotFoundException("Could not find question with ID " + id);
		}
	}

}
