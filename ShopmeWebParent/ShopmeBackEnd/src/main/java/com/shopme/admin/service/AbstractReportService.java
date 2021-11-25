package com.shopme.admin.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shopme.admin.dto.ReportItemDTO;
import com.shopme.admin.util.ReportType;

public abstract class AbstractReportService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractReportService.class);

	protected DateFormat dateFormatter;

	public List<ReportItemDTO> getReportDataLast7Days(ReportType reportType) {
		
		LOGGER.info("AbstractReportService | getReportDataLast7Days is called");
		
		LOGGER.info("AbstractReportService | getReportDataLast7Days | reportType : " + reportType);
		
		return getReportDataLastXDays(7, reportType);
	}

	public List<ReportItemDTO> getReportDataLast28Days(ReportType reportType) {
		
		LOGGER.info("AbstractReportService | getReportDataLast28Days is called");
		
		LOGGER.info("AbstractReportService | getReportDataLast28Days | reportType : " + reportType);
		
		return getReportDataLastXDays(28, reportType);
	}

	protected List<ReportItemDTO> getReportDataLastXDays(int days, ReportType reportType) {
		
		LOGGER.info("AbstractReportService | getReportDataLastXDays is called");
		
		LOGGER.info("AbstractReportService | getReportDataLastXDays | days : " + days);
		LOGGER.info("AbstractReportService | getReportDataLastXDays | reportType : " + reportType);
		
		Date endTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -(days - 1));
		Date startTime = cal.getTime();
		
		LOGGER.info("AbstractReportService | getReportDataLastXDays | startTime : " + startTime.toString());
		LOGGER.info("AbstractReportService | getReportDataLastXDays | endTime : " + endTime.toString());
		

		dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

		return getReportDataByDateRangeInternal(startTime, endTime, reportType);
	}

	public List<ReportItemDTO> getReportDataLast6Months(ReportType reportType) {
		
		LOGGER.info("AbstractReportService | getReportDataLast6Months is called");
		
		LOGGER.info("AbstractReportService | getReportDataLast6Months | reportType : " + reportType);
		
		return getReportDataLastXMonths(6, reportType);
	}

	public List<ReportItemDTO> getReportDataLastYear(ReportType reportType) {
		
		LOGGER.info("AbstractReportService | getReportDataLastYear is called");
		
		LOGGER.info("AbstractReportService | getReportDataLastYear | reportType : " + reportType);
		
		return getReportDataLastXMonths(12, reportType);
	}

	protected List<ReportItemDTO> getReportDataLastXMonths(int months, ReportType reportType) {
		
		LOGGER.info("AbstractReportService | getReportDataLastXMonths is called");
		
		LOGGER.info("AbstractReportService | getReportDataLastXMonths | months : " + months);
		LOGGER.info("AbstractReportService | getReportDataLastXMonths | reportType : " + reportType);
		
		Date endTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -(months - 1));
		Date startTime = cal.getTime();

		LOGGER.info("AbstractReportService | getReportDataLastXMonths | startTime : " + startTime.toString());
		LOGGER.info("AbstractReportService | getReportDataLastXMonths | endTime : " + endTime.toString());
		
		dateFormatter = new SimpleDateFormat("yyyy-MM");

		return getReportDataByDateRangeInternal(startTime, endTime, reportType);
	}

	public List<ReportItemDTO> getReportDataByDateRange(Date startTime, Date endTime, ReportType reportType) {
		
		LOGGER.info("AbstractReportService | getReportDataLastXMonths is called");
		LOGGER.info("AbstractReportService | getReportDataLastXMonths | startTime : " + startTime.toString());
		LOGGER.info("AbstractReportService | getReportDataLastXMonths | endTime : " + endTime.toString());
		LOGGER.info("AbstractReportService | getReportDataLastXMonths | reportType : " + reportType);
		
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		return getReportDataByDateRangeInternal(startTime, endTime, reportType);
	}

	protected abstract List<ReportItemDTO> getReportDataByDateRangeInternal(
			Date startDate, Date endDate, ReportType reportType);
}
