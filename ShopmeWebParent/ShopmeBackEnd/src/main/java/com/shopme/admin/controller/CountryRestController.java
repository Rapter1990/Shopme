package com.shopme.admin.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.repository.CountryRepository;
import com.shopme.common.entity.Country;

@RestController
public class CountryRestController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CountryRestController.class);

	@Autowired 
	private CountryRepository repo;

	@GetMapping("/countries/list")
	public List<Country> listAll() {
		return repo.findAllByOrderByNameAsc();
	}

	@PostMapping("/countries/save")
	public String save(@RequestBody Country country) {
		Country savedCountry = repo.save(country);
		return String.valueOf(savedCountry.getId());
	}

	@DeleteMapping("/countries/delete/{id}")
	public void delete(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
	
	@PostMapping("/countries/check_unique")
	@ResponseBody
	public String checkUnique(@RequestBody Map<String,String> data) {
		
		String name = data.get("name");
		
		LOGGER.info("CountryRestController | checkUnique is called");
		
		LOGGER.info("CountryRestController | checkUnique | name : " + name);
		
		Country countryByName = repo.findByName(name);
		boolean isCreatingNew = (countryByName.getId() != null ? true : false);
		
		if (isCreatingNew) {
			if (countryByName != null) return "Duplicate";
		} else {
			if (countryByName != null && countryByName.getId() != null) {
				return "Duplicate";
			}
		}
		
		return "OK";
	}
	
}
