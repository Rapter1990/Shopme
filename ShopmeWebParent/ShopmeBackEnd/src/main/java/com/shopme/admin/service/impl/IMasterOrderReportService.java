package com.shopme.admin.service.impl;

import java.util.Date;
import java.util.List;

import com.shopme.admin.dto.ReportItemDTO;
import com.shopme.common.entity.order.Order;

public interface IMasterOrderReportService {

	public List<ReportItemDTO> getReportDataLast7Days();
	
}
