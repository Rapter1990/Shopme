package com.shopme.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.EmailSettingBag;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import com.shopme.repository.SettingRepository;
import com.shopme.service.impl.ISettingService;

@Service
@Transactional
public class SettingService implements ISettingService{

	@Autowired 
	private SettingRepository repo;
	
	@Override
	public List<Setting> getGeneralSettings() {
		// TODO Auto-generated method stub
		return repo.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}
	
	@Override
	public EmailSettingBag getEmailSettings() {
		List<Setting> settings = repo.findByCategory(SettingCategory.MAIL_SERVER);
		settings.addAll(repo.findByCategory(SettingCategory.MAIL_TEMPLATES));

		return new EmailSettingBag(settings);
	}

}
