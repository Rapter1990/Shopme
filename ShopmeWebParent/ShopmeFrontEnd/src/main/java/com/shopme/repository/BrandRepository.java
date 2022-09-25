package com.shopme.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopme.common.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Integer> {

}
