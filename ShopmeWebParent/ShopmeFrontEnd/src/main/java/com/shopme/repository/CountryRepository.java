package com.shopme.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Country;

public interface CountryRepository extends CrudRepository<Country, Integer>{
	
	public List<Country> findAllByOrderByNameAsc();
}
