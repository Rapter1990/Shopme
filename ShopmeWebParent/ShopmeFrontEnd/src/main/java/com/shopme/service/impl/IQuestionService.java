package com.shopme.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.question.Question;
import com.shopme.common.exception.ProductNotFoundException;

public interface IQuestionService {

	public List<Question> getTop3VotedQuestions(Integer productId);
	public Page<Question> listQuestionsOfProduct(String alias, int pageNum, String sortField, String sortDir);
	public int getNumberOfQuestions(Integer productId);
	public Page<Question> listQuestionsByCustomer(Customer customer, String keyword, int pageNum, 
			String sortField, String sortDir);
	public Question getByCustomerAndId(Customer customer, Integer questionId);
	public void saveNewQuestion(Question question, Customer asker, Integer productId) throws ProductNotFoundException;
}
