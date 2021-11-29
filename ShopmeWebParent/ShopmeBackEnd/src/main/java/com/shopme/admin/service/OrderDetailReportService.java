package com.shopme.admin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.dto.ReportItemDTO;
import com.shopme.admin.repository.OrderDetailRepository;
import com.shopme.admin.util.ReportType;
import com.shopme.common.entity.order.OrderDetail;

@Service
public class OrderDetailReportService extends AbstractReportService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailReportService.class);

	@Autowired private OrderDetailRepository repo;

	@Override
	protected List<ReportItemDTO> getReportDataByDateRangeInternal(
			Date startDate, Date endDate, ReportType reportType) {
		
		LOGGER.info("OrderDetailReportService | getReportDataByDateRangeInternal is called");
		
		LOGGER.info("OrderDetailReportService | getReportDataByDateRangeInternal | startDate: " + startDate.toString());
		LOGGER.info("OrderDetailReportService | getReportDataByDateRangeInternal | endDate: " + endDate.toString());
		LOGGER.info("OrderDetailReportService | getReportDataByDateRangeInternal | reportType: " + reportType.toString());
		
		List<OrderDetail> listOrderDetails = null;

		if (reportType.equals(ReportType.CATEGORY)) {
			listOrderDetails = repo.findWithCategoryAndTimeBetween(startDate, endDate);
		}else if(reportType.equals(ReportType.PRODUCT)) {
			listOrderDetails = repo.findWithProductAndTimeBetween(startDate, endDate);
		}
		
		LOGGER.info("OrderDetailReportService | getReportDataByDateRangeInternal | listOrderDetails size: " + listOrderDetails.size());

		printRawData(listOrderDetails, reportType);

		List<ReportItemDTO> listReportItems = new ArrayList<>();

		for (OrderDetail detail : listOrderDetails) {
			String identifier = "";
			
			if (reportType.equals(ReportType.CATEGORY)) {
				identifier = detail.getProduct().getCategory().getName();
			}else if(reportType.equals(ReportType.PRODUCT)) {
				identifier = detail.getProduct().getShortName();
			}
			
			LOGGER.info("OrderDetailReportService | getReportDataByDateRangeInternal | identifier: " + identifier);
			
			ReportItemDTO reportItem = new ReportItemDTO(identifier);
			
			LOGGER.info("OrderDetailReportService | getReportDataByDateRangeInternal | reportItem: " + reportItem.toString());

			float grossSales = detail.getSubtotal() + detail.getShippingCost();
			float netSales = detail.getSubtotal() - detail.getProductCost();
			
			LOGGER.info("OrderDetailReportService | getReportDataByDateRangeInternal | grossSales: " + grossSales);
			LOGGER.info("OrderDetailReportService | getReportDataByDateRangeInternal | netSales: " + netSales);

			int itemIndex = listReportItems.indexOf(reportItem);
			
			LOGGER.info("OrderDetailReportService | getReportDataByDateRangeInternal | itemIndex: " + itemIndex);

			if (itemIndex >= 0) {
				reportItem = listReportItems.get(itemIndex);
				reportItem.addGrossSales(grossSales);
				reportItem.addNetSales(netSales);
				reportItem.increaseProductsCount(detail.getQuantity());
				
				LOGGER.info("OrderDetailReportService | getReportDataByDateRangeInternal | reportItem: " + reportItem.toString());
				
			} else {
				
				listReportItems.add(new ReportItemDTO(identifier, grossSales, netSales, detail.getQuantity()));
				
				LOGGER.info("OrderDetailReportService | getReportDataByDateRangeInternal | listReportItems: " + listReportItems.toString());
			}
		}
		
		LOGGER.info("OrderDetailReportService | getReportDataByDateRangeInternal | listReportItems size: " + listReportItems.size());

		printReportData(listReportItems);

		return listReportItems;
	}

	private void printReportData(List<ReportItemDTO> listReportItems) {
		
		LOGGER.info("OrderDetailReportService | printReportData is called");
		
		LOGGER.info("OrderDetailReportService | printReportData | listReportItems size: " + listReportItems.size());
		
		for (ReportItemDTO item : listReportItems) {
			System.out.printf("%-20s, %10.2f, %10.2f, %d \n",
					item.getIdentifier(), item.getGrossSales(), item.getNetSales(), item.getProductsCount());
		}
	}

	private void printRawData(List<OrderDetail> listOrderDetails, ReportType reportType) {
		
		LOGGER.info("OrderDetailReportService | printRawData is called");
		
		LOGGER.info("OrderDetailReportService | printRawData | listOrderDetails size: " + listOrderDetails.size());
		
		for (OrderDetail detail : listOrderDetails) {
			if(reportType.equals(ReportType.CATEGORY)) {
				System.out.printf("%d, %-20s, %10.2f, %10.2f, %10.2f \n",
						detail.getQuantity(), detail.getProduct().getCategory().getName(),
						detail.getSubtotal(), detail.getProductCost(), detail.getShippingCost());
			}else if(reportType.equals(ReportType.PRODUCT)) {
				System.out.printf("%d, %-20s, %10.2f, %10.2f, %10.2f \n",
						detail.getQuantity(), detail.getProduct().getShortName().substring(0, 20),
						detail.getSubtotal(), detail.getProductCost(), detail.getShippingCost());
			}
			
		}
	}
}
