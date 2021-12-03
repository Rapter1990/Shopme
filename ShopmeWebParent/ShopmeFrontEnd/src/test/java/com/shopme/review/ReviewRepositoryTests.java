package com.shopme.review;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.shopme.common.entity.Review;
import com.shopme.repository.ReviewRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ReviewRepositoryTests {

	@Autowired 
	private ReviewRepository repo;

	@Test
	public void testFindByCustomerNoKeyword() {
		Integer customerId = 8;
		Pageable pageable = PageRequest.of(1, 5);

		Page<Review> page = repo.findByCustomer(customerId, pageable);
		long totalElements = page.getTotalElements();

		assertThat(totalElements).isGreaterThan(1);		
	}

	@Test
	public void testFindByCustomerWithKeyword() {
		Integer customerId = 8;
		String keyword = "charger";
		Pageable pageable = PageRequest.of(1, 5);

		Page<Review> page = repo.findByCustomer(customerId, keyword, pageable);
		long totalElements = page.getTotalElements();

		assertThat(totalElements).isGreaterThan(0);		
	}

	@Test
	public void testFindByCustomerAndId() {
		Integer customerId = 8;
		Integer reviewId = 3;

		Review review = repo.findByCustomerAndId(customerId, reviewId);
		assertThat(review).isNotNull();
	}
}