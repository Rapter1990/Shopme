package com.shopme.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.dto.ReportItemDTO;
import com.shopme.admin.service.MasterOrderReportService;


@RestController
public class ReportRestController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportRestController.class);
	
	@Autowired 
	private MasterOrderReportService masterOrderReportService;

	@GetMapping("/reports/sales_by_date/{period}")
	public List<ReportItemDTO> getReportDataByDatePeriod(@PathVariable("period") String period) {
		
		LOGGER.info("ReportRestController | getReportDataByDatePeriod is called");
		
		LOGGER.info("ReportRestController | getReportDataByDatePeriod | Report period: " + period);
		
		switch (period) {
			case "last_7_days":
				
				LOGGER.info("ReportRestController | getReportDataByDatePeriod | case last_7_days");
				return masterOrderReportService.getReportDataLast7Days();
	
			case "last_28_days":
				
				LOGGER.info("ReportRestController | getReportDataByDatePeriod | case last_28_days");
				return masterOrderReportService.getReportDataLast28Days();
				
			default:
				
				LOGGER.info("ReportRestController | getReportDataByDatePeriod | default last_7_days");
				return masterOrderReportService.getReportDataLast7Days();
		}
	}
}
