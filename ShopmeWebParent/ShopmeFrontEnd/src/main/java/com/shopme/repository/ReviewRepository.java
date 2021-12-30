package com.shopme.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Review;
import com.shopme.common.entity.product.Product;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

	@Query("SELECT r FROM Review r WHERE r.customer.id = ?1")
	public Page<Review> findByCustomer(Integer customerId, Pageable pageable);

	@Query("SELECT r FROM Review r WHERE r.customer.id = ?1 AND ("
			+ "r.headline LIKE %?2% OR r.comment LIKE %?2% OR "
			+ "r.product.name LIKE %?2%)")
	public Page<Review> findByCustomer(Integer customerId, String keyword, Pageable pageable);

	@Query("SELECT r FROM Review r WHERE r.customer.id = ?1 AND r.id = ?2")
	public Review findByCustomerAndId(Integer customerId, Integer reviewId);
	
	public Page<Review> findByProduct(Product product, Pageable pageable);
	
	@Query("SELECT COUNT(r.id) FROM Review r WHERE r.customer.id = ?1 AND "
			+ "r.product.id = ?2")
	public Long countByCustomerAndProduct(Integer customerId, Integer productId);
	
	@Query("UPDATE Review r SET r.votes = COALESCE((SELECT SUM(v.votes) FROM ReviewVote v"
			+ " WHERE v.review.id=?1), 0) WHERE r.id = ?1")
	@Modifying
	public void updateVoteCount(Integer reviewId);
	
	@Query("SELECT r.votes FROM Review r WHERE r.id = ?1")
	public Integer getVoteCount(Integer reviewId);
}
