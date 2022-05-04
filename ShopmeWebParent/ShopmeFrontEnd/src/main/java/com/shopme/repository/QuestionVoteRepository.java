package com.shopme.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.question.QuestionVote;

public interface QuestionVoteRepository extends CrudRepository<QuestionVote, Integer> {

	@Query("SELECT qv FROM QuestionVote qv WHERE qv.question.id = ?1 AND qv.customer.id = ?2")
	public QuestionVote findByQuestionAndCustomer(Integer questionId, Integer customerId);

	@Query("SELECT qv FROM QuestionVote qv WHERE qv.question.product.id = ?1 AND qv.customer.id = ?2")
	public List<QuestionVote> findByProductAndCustomer(Integer productId, Integer customerId);

}
