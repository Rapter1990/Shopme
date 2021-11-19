package com.shopme.admin.service.impl;

import java.util.List;
import com.shopme.admin.dto.ReportItemDTO;

public interface IMasterOrderReportService {

	public List<ReportItemDTO> getReportDataLast7Days();
	public List<ReportItemDTO> getReportDataLast28Days();
	
}
