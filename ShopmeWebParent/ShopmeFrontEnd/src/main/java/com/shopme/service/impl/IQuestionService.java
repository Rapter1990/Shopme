package com.shopme.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;

import com.shopme.common.entity.question.Question;

public interface IQuestionService {

	public List<Question> getTop3VotedQuestions(Integer productId);
	public Page<Question> listQuestionsOfProduct(String alias, int pageNum, String sortField, String sortDir);
	public int getNumberOfQuestions(Integer productId);
}
