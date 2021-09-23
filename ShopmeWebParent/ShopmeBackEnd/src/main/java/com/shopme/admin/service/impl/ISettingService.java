package com.shopme.admin.service.impl;

import java.util.List;

import com.shopme.admin.util.GeneralSettingBag;
import com.shopme.common.entity.setting.Setting;

public interface ISettingService {

	public List<Setting> listAllSettings();
	
	public GeneralSettingBag getGeneralSettings();
	
	public void saveAll(Iterable<Setting> settings);
	
	public List<Setting> getMailServerSettings();
	
	public List<Setting> getMailTemplateSettings();

	public List<Setting> getCurrencySettings();

	public List<Setting> getPaymentSettings();
}
