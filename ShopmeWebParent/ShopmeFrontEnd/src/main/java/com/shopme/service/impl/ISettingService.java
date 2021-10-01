package com.shopme.service.impl;

import java.util.List;

import com.shopme.common.entity.EmailSettingBag;
import com.shopme.common.entity.setting.Setting;
import com.shopme.setting.CurrencySettingBag;
import com.shopme.setting.PaymentSettingBag;

public interface ISettingService {

	public List<Setting> getGeneralSettings();
	public EmailSettingBag getEmailSettings();
	public CurrencySettingBag getCurrencySettings();
	public String getCurrencyCode();
	public PaymentSettingBag getPaymentSettings();
}
