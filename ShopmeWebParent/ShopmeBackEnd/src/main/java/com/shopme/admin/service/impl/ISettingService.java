package com.shopme.admin.service.impl;

import java.util.List;

import com.shopme.admin.util.GeneralSettingBag;
import com.shopme.common.entity.Setting;

public interface ISettingService {

	public List<Setting> listAllSettings();
	
	public GeneralSettingBag getGeneralSettings();
	
	public void saveAll(Iterable<Setting> settings);
}
