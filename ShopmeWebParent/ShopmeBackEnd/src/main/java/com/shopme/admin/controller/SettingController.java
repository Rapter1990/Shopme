package com.shopme.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.admin.repository.CurrencyRepository;
import com.shopme.admin.service.SettingService;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;

@Controller
public class SettingController {

	private SettingService service;
	
	private CurrencyRepository currencyRepo;
	
	@Autowired
	public SettingController(SettingService service, CurrencyRepository currencyRepo) {
		super();
		this.service = service;
		this.currencyRepo = currencyRepo;
	}
	
	@GetMapping("/settings")
	public String listAll(Model model) {
		List<Setting> listSettings = service.listAllSettings();
		List<Currency> listCurrencies = currencyRepo.findAllByOrderByNameAsc();

		model.addAttribute("listCurrencies", listCurrencies);

		for (Setting setting : listSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}

		return "settings/settings";
	}
}
