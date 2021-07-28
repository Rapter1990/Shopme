package com.shopme.service.impl;

import java.util.List;

import com.shopme.common.entity.EmailSettingBag;
import com.shopme.common.entity.Setting;

public interface ISettingService {

	public List<Setting> getGeneralSettings();
	public EmailSettingBag getEmailSettings();
}
