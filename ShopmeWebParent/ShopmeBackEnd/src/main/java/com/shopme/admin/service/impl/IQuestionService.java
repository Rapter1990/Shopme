package com.shopme.admin.service.impl;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.User;
import com.shopme.common.entity.question.Question;
import com.shopme.common.exception.QuestionNotFoundException;

public interface IQuestionService {

	public void listByPage(int pageNum, PagingAndSortingHelper helper);
	public Question getQuestionById(Integer id) throws QuestionNotFoundException;
	public void saveQuestionByUser(Question questionInForm, User user) throws QuestionNotFoundException;
	public void approve(Integer id);
	public void disapprove(Integer id);
	public void delete(Integer id) throws QuestionNotFoundException;
}
