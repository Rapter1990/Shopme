package com.shopme.admin.util;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shopme.admin.dto.ReportItemDTO;
import com.shopme.common.entity.order.Order;

public class MasterOrderReportServiceUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MasterOrderReportServiceUtil.class);
	
	
	public static void calculateSalesForReportData(List<Order> listOrders, List<ReportItemDTO> listReportItems, 
			DateFormat dateFormatter) {
		// TODO Auto-generated method stub
		
		LOGGER.info("MasterOrderReportServiceUtil | calculateSalesForReportData is called");
		
		for (Order order : listOrders) {
			String orderDateString = dateFormatter.format(order.getOrderTime());
			
			LOGGER.info("MasterOrderReportServiceUtil | calculateSalesForReportData | orderDateString : " + orderDateString);

			ReportItemDTO reportItem = new ReportItemDTO(orderDateString);
			
			LOGGER.info("MasterOrderReportServiceUtil | calculateSalesForReportData | reportItem : " + reportItem.toString());

			int itemIndex = listReportItems.indexOf(reportItem);
			
			LOGGER.info("MasterOrderReportServiceUtil | calculateSalesForReportData | itemIndex : " + itemIndex);

			if (itemIndex >= 0) {
				reportItem = listReportItems.get(itemIndex);
				
				reportItem.addGrossSales(order.getTotal());
				reportItem.addNetSales(order.getSubtotal() - order.getProductCost());
				reportItem.increaseOrdersCount();
				
				LOGGER.info("MasterOrderReportServiceUtil | calculateSalesForReportData | reportItem : " + reportItem.toString());
			}
		}
		
	}

	public static void printReportData(List<ReportItemDTO> listReportItems) {
		// TODO Auto-generated method stub
		
		LOGGER.info("MasterOrderReportServiceUtil | printReportData is called");
		
		listReportItems.forEach(item -> {
			System.out.printf("%s, %10.2f, %10.2f, %d \n", item.getIdentifier(), item.getGrossSales(),
					item.getNetSales(), item.getOrdersCount());
		});
		
	}

	public static List<ReportItemDTO> createReportData(Date startTime, Date endTime, 
			DateFormat dateFormatter, ReportType reportType) {
		// TODO Auto-generated method stub
		
		LOGGER.info("MasterOrderReportServiceUtil | createReportData is called");
		
		List<ReportItemDTO> listReportItems = new ArrayList<>();

		Calendar startDate = Calendar.getInstance();
		startDate.setTime(startTime);
		
		LOGGER.info("MasterOrderReportServiceUtil | createReportData | startDate : " + startDate.toString());

		Calendar endDate = Calendar.getInstance();
		endDate.setTime(endTime);	
		
		LOGGER.info("MasterOrderReportServiceUtil | createReportData | endDate : " + endDate.toString());

		Date currentDate = startDate.getTime();
		
		LOGGER.info("MasterOrderReportServiceUtil | createReportData | currentDate : " + currentDate.toString());
		LOGGER.info("MasterOrderReportServiceUtil | createReportData | endDate : " + endDate.getTime().toString());
		
		String dateString = dateFormatter.format(currentDate);
		
		LOGGER.info("MasterOrderReportServiceUtil | createReportData | dateString : " + dateString);

		listReportItems.add(new ReportItemDTO(dateString));
		
		LOGGER.info("MasterOrderReportServiceUtil | createReportData | listReportItems : " + listReportItems.size());

		do {
			
			if (reportType.equals(ReportType.DAY)) {
				
				LOGGER.info("MasterOrderReportServiceUtil | createReportData | reportType : " + reportType);
				
				startDate.add(Calendar.DAY_OF_MONTH, 1);
				
				LOGGER.info("MasterOrderReportServiceUtil | createReportData | startDate : " + startDate.toString());
				
			} else if (reportType.equals(ReportType.MONTH)) {
				
				LOGGER.info("MasterOrderReportServiceUtil | createReportData | reportType : " + reportType);
				
				startDate.add(Calendar.MONTH, 1);
				
				LOGGER.info("MasterOrderReportServiceUtil | createReportData | startDate : " + startDate.toString());
				
			}
					
			currentDate = startDate.getTime();
			
			LOGGER.info("MasterOrderReportServiceUtil | createReportData | currentDate : " + currentDate);
			
			dateString = dateFormatter.format(currentDate);	
			
			LOGGER.info("MasterOrderReportServiceUtil | createReportData | dateString : " + dateString);

			listReportItems.add(new ReportItemDTO(dateString));
			
			LOGGER.info("MasterOrderReportServiceUtil | createReportData | listReportItems : " + listReportItems.size());

		} while (startDate.before(endDate));
		
		LOGGER.info("MasterOrderReportServiceUtil | createReportData | listReportItems : " + listReportItems.size());

		LOGGER.info("MasterOrderReportServiceUtil | createReportData | listReportItems : " + listReportItems.toString());
		
		return listReportItems;	
	}

	public static void printRawData(List<Order> listOrders) {
		// TODO Auto-generated method stub
		
		LOGGER.info("MasterOrderReportServiceUtil | printRawData is called");
		
		listOrders.forEach(order -> {
			System.out.printf("%-3d | %s | %10.2f | %10.2f \n",
					order.getId(), order.getOrderTime(), order.getTotal(), order.getProductCost());
		});
		
	}
	
}
