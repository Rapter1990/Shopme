package com.shopme.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.shopme.admin.repository.SettingRepository;
import com.shopme.admin.service.impl.ISettingService;
import com.shopme.common.entity.Setting;


public class SettingService implements ISettingService{

	@Autowired 
	private SettingRepository repo;
	
	@Override
	public List<Setting> listAllSettings() {
		// TODO Auto-generated method stub
		return (List<Setting>) repo.findAll();
	}

}
