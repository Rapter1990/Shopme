package com.shopme.service.impl;

import java.util.List;

import com.shopme.common.entity.question.Question;

public interface IQuestionService {

	public List<Question> getTop3VotedQuestions(Integer productId);
}
