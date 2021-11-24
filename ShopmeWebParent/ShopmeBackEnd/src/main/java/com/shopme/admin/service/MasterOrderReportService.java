package com.shopme.admin.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.shopme.admin.dto.ReportItemDTO;
import com.shopme.admin.repository.OrderRepository;
import com.shopme.admin.util.MasterOrderReportServiceUtil;
import com.shopme.admin.util.ReportType;
import com.shopme.common.entity.order.Order;

@Service
public class MasterOrderReportService extends AbstractReportService{

	private static final Logger LOGGER = LoggerFactory.getLogger(MasterOrderReportService.class);
	
	private final OrderRepository repo;
	
	public MasterOrderReportService(OrderRepository repo) {
		super();
		this.repo = repo;
	}

	@Override
	protected List<ReportItemDTO> getReportDataByDateRangeInternal(Date startTime, Date endTime,
			ReportType reportType) {
		
		LOGGER.info("MasterOrderReportService | getReportDataByDateRange is called");
		
		List<Order> listOrders = repo.findByOrderTimeBetween(startTime, endTime);
		MasterOrderReportServiceUtil.printRawData(listOrders);

		List<ReportItemDTO> listReportItems = MasterOrderReportServiceUtil.createReportData(startTime, endTime, dateFormatter, reportType);

		System.out.println();

		MasterOrderReportServiceUtil.calculateSalesForReportData(listOrders, listReportItems, dateFormatter);
		
		MasterOrderReportServiceUtil.printReportData(listReportItems);
		
		return listReportItems;
	}
	

}
