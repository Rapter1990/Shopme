package com.shopme.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

public interface StateRepository extends CrudRepository<State, Integer> {

	public List<State> findByCountryOrderByNameAsc(Country country);
	
	@Query("SELECT s FROM State s LEFT JOIN s.country ON s.country.id = s.id WHERE s.name = :name")
	public State findByName(String name);
}
