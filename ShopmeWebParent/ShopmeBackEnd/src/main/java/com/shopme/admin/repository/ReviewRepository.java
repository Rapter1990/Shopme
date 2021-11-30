package com.shopme.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopme.common.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

}
