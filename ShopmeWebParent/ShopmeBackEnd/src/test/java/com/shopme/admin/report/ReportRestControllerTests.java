package com.shopme.admin.report;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportRestControllerTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReportRestControllerTests.class);
	
	@Autowired 
	private MockMvc mockMvc;

	@Test
	@WithMockUser(username = "user1", password = "pass1", authorities = {"Salesperson"})
	public void testGetReportDataLast7Days() throws Exception {
		
		LOGGER.info("ReportRestControllerTests | testGetReportDataLast7Days is called");
		
		String requestURL = "/reports/sales_by_date/last_7_days";
		
		LOGGER.info("ReportRestControllerTests | testGetReportDataLast7Days | requestURL: " + requestURL);

		mockMvc.perform(get(requestURL)).andExpect(status().isOk()).andDo(print());
	}
	
	@Test
	@WithMockUser(username = "user1", password = "pass1", authorities = {"Salesperson"})
	public void testGetReportDataLast6Months() throws Exception {
		
		LOGGER.info("ReportRestControllerTests | testGetReportDataLast6Months is called");
		
		String requestURL = "/reports/sales_by_date/last_6_months";
		
		LOGGER.info("ReportRestControllerTests | testGetReportDataLast6Months | requestURL: " + requestURL);

		mockMvc.perform(get(requestURL)).andExpect(status().isOk()).andDo(print());
	}	
}
