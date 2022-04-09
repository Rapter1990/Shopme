package com.shopme.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.question.Question;
import com.shopme.repository.QuestionRepository;
import com.shopme.service.impl.IQuestionService;

@Service
@Transactional
public class QuestionService implements IQuestionService{

	public static final int QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING = 10;
	public static final int QUESTIONS_PER_PAGE_FOR_CUSTOMER = 4;
	
	private QuestionRepository questionRepo;
	
	
	@Autowired
	public QuestionService(QuestionRepository questionRepo) {
		super();
		this.questionRepo = questionRepo;
	}


	@Override
	public List<Question> getTop3VotedQuestions(Integer productId) {
		Pageable pageable = PageRequest.of(0, 3, Sort.by("votes").descending());
		Page<Question> result = questionRepo.findAll(productId, pageable);
		return result.getContent();
	}
}
