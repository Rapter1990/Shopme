package com.shopme.admin.service;

import java.text.DateFormat;
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

}
