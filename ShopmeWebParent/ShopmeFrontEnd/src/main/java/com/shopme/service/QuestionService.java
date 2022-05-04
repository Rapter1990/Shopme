package com.shopme.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.question.Question;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.repository.ProductRepository;
import com.shopme.repository.QuestionRepository;
import com.shopme.service.impl.IQuestionService;

@Service
@Transactional
public class QuestionService implements IQuestionService{

	public static final int QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING = 10;
	public static final int QUESTIONS_PER_PAGE_FOR_CUSTOMER = 4;
	
	private QuestionRepository questionRepo;
	
	private ProductRepository productRepo;
	
	@Autowired
	public QuestionService(QuestionRepository questionRepo, ProductRepository productRepo) {
		super();
		this.questionRepo = questionRepo;
		this.productRepo = productRepo;
	}


	@Override
	public List<Question> getTop3VotedQuestions(Integer productId) {
		Pageable pageable = PageRequest.of(0, 3, Sort.by("votes").descending());
		Page<Question> result = questionRepo.findAll(productId, pageable);
		return result.getContent();
	}
	
	@Override
	public Page<Question> listQuestionsOfProduct(String alias, int pageNum, String sortField, String sortDir) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending(); 
		Pageable pageable = PageRequest.of(pageNum - 1, QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING, sort);
		return questionRepo.findByAlias(alias, pageable);
	}
	
	@Override
	public int getNumberOfQuestions(Integer productId) {
		return questionRepo.countApprovedQuestions(productId);
	}
	
	@Override
	public Page<Question> listQuestionsByCustomer(Customer customer, String keyword, int pageNum, 
			String sortField, String sortDir) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, QUESTIONS_PER_PAGE_FOR_CUSTOMER, sort);

		if (keyword != null) {
			return questionRepo.findByCustomer(customer.getId(), keyword, pageable);
		}

		return questionRepo.findByCustomer(customer.getId(), pageable);
	}
	
	public Question getByCustomerAndId(Customer customer, Integer questionId) {
		return questionRepo.findByCustomerAndId(customer.getId(), questionId);
	}
	
	public void saveNewQuestion(Question question, Customer asker, 
			Integer productId) throws ProductNotFoundException {
		Optional<Product> productById = productRepo.findById(productId);
		if (!productById.isPresent()) {
			throw new ProductNotFoundException("Could not find product with ID " + productId);
		}
		question.setAskTime(new Date());
		question.setProduct(productById.get());
		question.setAsker(asker);

		questionRepo.save(question);
	}
	
	public int getNumberOfAnsweredQuestions(Integer productId) {
		return questionRepo.countAnsweredQuestions(productId);
	}


	public int getVotesForQuestion(Integer questionId) {
		Question question = questionRepo.findById(questionId).get();
		return question.getVotes();
	}
}
