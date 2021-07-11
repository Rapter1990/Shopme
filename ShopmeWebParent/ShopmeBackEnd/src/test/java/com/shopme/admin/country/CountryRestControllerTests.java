package com.shopme.admin.country;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.repository.CountryRepository;
import com.shopme.common.entity.Country;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTests {

	MockMvc mockMvc;

	ObjectMapper objectMapper;

	CountryRepository repo;
	
	@Autowired
	public CountryRestControllerTests(MockMvc mockMvc, ObjectMapper objectMapper, CountryRepository repo) {
		super();
		this.mockMvc = mockMvc;
		this.objectMapper = objectMapper;
		this.repo = repo;
	}

	@Test
	@WithMockUser(username = "ya@a.com", password = "12345678", roles = "ADMIN")
	public void testListCountries() throws Exception {
		String url = "/countries/list";

		MvcResult result = mockMvc.perform(get(url))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();

		String jsonResponse = result.getResponse().getContentAsString();		
		Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);

		assertThat(countries).hasSizeGreaterThan(0);
	}

	@Test
	@WithMockUser(username = "ya@a.com", password = "12345678", roles = "ADMIN")
	public void testCreateCountry() throws JsonProcessingException, Exception {
		String url = "/countries/save";
		String countryName = "Germany";
		String countryCode = "DE";
		Country country = new Country(countryName, countryCode);

		MvcResult result = mockMvc.perform(post(url).contentType("application/json")
				.content(objectMapper.writeValueAsString(country))
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		String response = result.getResponse().getContentAsString();
		Integer countryId = Integer.parseInt(response);

		Optional<Country> findById = repo.findById(countryId);
		assertThat(findById.isPresent());

		Country savedCountry = findById.get();

		assertThat(savedCountry.getName()).isEqualTo(countryName);
	}

	@Test
	@WithMockUser(username = "ya@a.com", password = "12345678", roles = "ADMIN")
	public void testUpdateCountry() throws JsonProcessingException, Exception {
		String url = "/countries/save";

		Integer countryId = 7;
		String countryName = "China";
		String countryCode = "CN";
		Country country = new Country(countryId, countryName, countryCode);

		mockMvc.perform(post(url).contentType("application/json")
				.content(objectMapper.writeValueAsString(country))
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(String.valueOf(countryId)));

		Optional<Country> findById = repo.findById(countryId);
		assertThat(findById.isPresent());

		Country savedCountry = findById.get();

		assertThat(savedCountry.getName()).isEqualTo(countryName);
	}

	@Test
	@WithMockUser(username = "ya@a.com", password = "12345678", roles = "ADMIN")
	public void testDeleteCountry() throws Exception {
		Integer countryId = 7;
		String url = "/countries/delete/" + countryId;
		
		mockMvc.perform(get(url)).andExpect(status().isOk());

		Optional<Country> findById = repo.findById(countryId);

		assertThat(findById).isNotPresent();
	}
}
