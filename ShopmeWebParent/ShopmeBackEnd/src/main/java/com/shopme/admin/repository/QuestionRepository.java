package com.shopme.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.question.Question;

public interface QuestionRepository extends SearchRepository<Question, Integer>{

	@Query("SELECT q FROM Question q WHERE q.questionContent LIKE %?1% OR "
			+ "q.answer LIKE %?1% OR q.product.name LIKE %?1% OR "
			+ "CONCAT(q.asker.firstName, ' ', q.asker.lastName) LIKE %?1%")
	public Page<Question> findAll(String keyword, Pageable pageable);

	@Query("UPDATE Question p SET p.approved = ?2 WHERE p.id = ?1")
	@Modifying
	public void updateApprovalStatus(Integer id, boolean enabled);
}
