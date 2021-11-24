package com.shopme.admin.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shopme.admin.service.SettingService;
import com.shopme.common.entity.setting.Setting;

public class ReportUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportUtil.class);

	public static void loadCurrencySetting(HttpServletRequest request, SettingService settingService) {
		
		LOGGER.info("ReportUtil | loadCurrencySetting is called");
    	
		List<Setting> currencySettings = settingService.getCurrencySettings();
		
		LOGGER.info("ReportUtil | loadCurrencySetting | currencySettings : " + currencySettings.toString());
		
		for (Setting setting : currencySettings) {
			LOGGER.info("ReportUtil | loadCurrencySetting | setting | key : " 
		                 + setting.getKey() + " , value : " + setting.getValue());
			request.setAttribute(setting.getKey(), setting.getValue());
		}	
	}
}
