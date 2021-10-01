package com.shopme.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Currency;
import com.shopme.common.entity.EmailSettingBag;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;
import com.shopme.repository.CurrencyRepository;
import com.shopme.repository.SettingRepository;
import com.shopme.service.impl.ISettingService;
import com.shopme.setting.CurrencySettingBag;
import com.shopme.setting.PaymentSettingBag;

@Service
@Transactional
public class SettingService implements ISettingService{

	private SettingRepository settingRepo;
	
	private CurrencyRepository currencyRepo;
	
	@Autowired
	public SettingService(SettingRepository settingRepo, CurrencyRepository currencyRepo) {
		super();
		this.settingRepo = settingRepo;
		this.currencyRepo = currencyRepo;
	}

	@Override
	public List<Setting> getGeneralSettings() {
		// TODO Auto-generated method stub
		return settingRepo.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}
	
	@Override
	public EmailSettingBag getEmailSettings() {
		List<Setting> settings = settingRepo.findByCategory(SettingCategory.MAIL_SERVER);
		settings.addAll(settingRepo.findByCategory(SettingCategory.MAIL_TEMPLATES));

		return new EmailSettingBag(settings);
	}
	
	@Override
	public CurrencySettingBag getCurrencySettings() {
		List<Setting> settings = settingRepo.findByCategory(SettingCategory.CURRENCY);
		return new CurrencySettingBag(settings);
	}
	
	@Override
	public PaymentSettingBag getPaymentSettings() {
		List<Setting> settings = settingRepo.findByCategory(SettingCategory.PAYMENT);
		return new PaymentSettingBag(settings);
	}

	@Override
	public String getCurrencyCode() {
		Setting setting = settingRepo.findByKey("CURRENCY_ID");
		Integer currencyId = Integer.parseInt(setting.getValue());
		Currency currency = currencyRepo.findById(currencyId).get();

		return currency.getCode();
	}

}
