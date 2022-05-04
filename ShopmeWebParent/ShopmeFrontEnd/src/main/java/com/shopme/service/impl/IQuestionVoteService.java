package com.shopme.service.impl;

import java.util.List;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.VoteResultDTO;
import com.shopme.common.entity.VoteType;
import com.shopme.common.entity.question.Question;
import com.shopme.common.entity.question.QuestionVote;

public interface IQuestionVoteService {

	public VoteResultDTO doVote(Integer questionId, Customer customer, VoteType voteType);
	public VoteResultDTO undoVote(QuestionVote vote, Integer questionId, VoteType voteType);
	public void markQuestionsVotedForProductByCustomer(List<Question> listQuestions, 
			Integer productId, Integer customerId);
	
	
}
