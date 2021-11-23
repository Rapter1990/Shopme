package com.shopme.admin.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.shopme.admin.dto.ReportItemDTO;
import com.shopme.admin.repository.OrderRepository;
import com.shopme.admin.service.impl.IMasterOrderReportService;
import com.shopme.admin.util.MasterOrderReportServiceUtil;

@Service
public class MasterOrderReportService implements IMasterOrderReportService{

	private static final Logger LOGGER = LoggerFactory.getLogger(MasterOrderReportService.class);
	
	private final OrderRepository repo;
	private DateFormat dateFormatter;
	
	public MasterOrderReportService(OrderRepository repo) {
		super();
		this.repo = repo;
	}

	@Override
	public List<ReportItemDTO> getReportDataLast7Days() {
		// TODO Auto-generated method stub
		
		LOGGER.info("MasterOrderReportService | getReportDataLast7Days is called");
		
		return MasterOrderReportServiceUtil.getReportDataLastXDays(repo, dateFormatter ,7);
	}

	@Override
	public List<ReportItemDTO> getReportDataLast28Days() {
		// TODO Auto-generated method stub
		
		LOGGER.info("MasterOrderReportService | getReportDataLast28Days is called");
		
		return MasterOrderReportServiceUtil.getReportDataLastXDays(repo, dateFormatter ,28);
	}
	
	public List<ReportItemDTO> getReportDataLast6Months() {
		
		LOGGER.info("MasterOrderReportService | getReportDataLast6Months is called");
		
		return MasterOrderReportServiceUtil.getReportDataLastXMonths(repo, dateFormatter ,6);
	}

	public List<ReportItemDTO> getReportDataLastYear() {
		
		LOGGER.info("MasterOrderReportService | getReportDataLastYear is called");
		
		return MasterOrderReportServiceUtil.getReportDataLastXMonths(repo, dateFormatter ,12);
	}
	
	public List<ReportItemDTO> getReportDataByDateRange(Date startTime, Date endTime) {
		
		LOGGER.info("MasterOrderReportService | getReportDataByDateRange is called");
		
		return MasterOrderReportServiceUtil.getReportDataByDateRange(repo, startTime, endTime, dateFormatter);
	}
	

}
