package com.shopme.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);
	
	@GetMapping("/reports")
	public String viewSalesReportHome() {
		
		LOGGER.info("ReportController | viewSalesReportHome is called");
		
		return "reports/reports";
	}
	
	
}
