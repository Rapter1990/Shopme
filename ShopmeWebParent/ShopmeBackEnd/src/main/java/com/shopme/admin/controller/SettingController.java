package com.shopme.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.helper.SettingHelper;
import com.shopme.admin.repository.CurrencyRepository;
import com.shopme.admin.service.SettingService;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.admin.util.GeneralSettingBag;
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
	
	@PostMapping("/settings/save_general")
	public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,
			HttpServletRequest request, RedirectAttributes ra) throws IOException {
		GeneralSettingBag settingBag = service.getGeneralSettings();

		SettingHelper.saveSiteLogo(multipartFile, settingBag);
		SettingHelper.saveCurrencySymbol(request, settingBag,currencyRepo);

		SettingHelper.updateSettingValuesFromForm(request, settingBag.list(),service);

		ra.addFlashAttribute("messageSuccess", "General settings have been saved.");

		return "redirect:/settings";
	}

	
}
