package com.shopme.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.service.SectionService;
import com.shopme.common.entity.section.Section;
import com.shopme.common.exception.SectionNotFoundException;
import com.shopme.common.exception.SectionUnmoveableException;

@Controller
public class GeneralSectionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralSectionController.class);
	
	@Autowired 
	private SectionService service;

	@GetMapping("/sections")
	public String listAllSections(Model model) {
		
		LOGGER.info("GeneralSectionController | listAllSections is called");
		
		List<Section> listSections = service.listSections();
		
		LOGGER.info("GeneralSectionController | listAllSections | listSections size : " + listSections.size());
		model.addAttribute("listSections", listSections);

		return "sections/sections";
	}

	@GetMapping("/sections/delete/{id}")
	public String deleteSection(@PathVariable(name = "id") Integer id, RedirectAttributes ra) {
		
		LOGGER.info("GeneralSectionController | deleteSection is called");
		
		try {
			service.deleteSection(id);
			
			LOGGER.info("GeneralSectionController | deleteSection | messageSuccess : " + "The section ID " + id + " has been deleted.");
			ra.addFlashAttribute("messageSuccess", "The section ID " + id + " has been deleted.");

		} catch (SectionNotFoundException ex) {
			LOGGER.info("GeneralSectionController | deleteSection | messageError : " + ex.getMessage());
			ra.addFlashAttribute("messageError", ex.getMessage());
		}

		return "redirect:/sections";		
	}

	@GetMapping("/sections/{id}/enabled/{enabledStatus}")
	public String updateSectionEnabledStatus(@PathVariable(name = "id") Integer id, @PathVariable("enabledStatus") String enabledStatus,
			RedirectAttributes ra) {
		
		LOGGER.info("GeneralSectionController | updateSectionEnabledStatus is called");
		
		try {
			boolean enabled = Boolean.parseBoolean(enabledStatus);
			
			LOGGER.info("GeneralSectionController | updateSectionEnabledStatus | enabled : " + enabled);
			
			service.updateSectionEnabledStatus(id, enabled);
			String updateResult = enabled ? "enabled." : "disabled.";
			
			LOGGER.info("GeneralSectionController | updateSectionEnabledStatus | updateResult : " + updateResult);
			
			LOGGER.info("GeneralSectionController | updateSectionEnabledStatus | messageSuccess : " + "The section ID " + id + " has been " + updateResult);
			ra.addFlashAttribute("messageSuccess", "The section ID " + id + " has been " + updateResult);

		} catch (SectionNotFoundException ex) {
			LOGGER.info("GeneralSectionController | updateSectionEnabledStatus | messageError : " + ex.getMessage());
			ra.addFlashAttribute("messageError", ex.getMessage());
		}

		return "redirect:/sections";
	}

	@GetMapping("/sections/moveup/{id}")
	public String moveSectionUp(@PathVariable(name = "id") Integer id, RedirectAttributes ra) {
		
		LOGGER.info("GeneralSectionController | moveSectionUp is called");
		
		try {
			service.moveSectionUp(id);

			LOGGER.info("GeneralSectionController | moveSectionUp | messageSuccess : " + "The section ID " + id + " has been moved up by one position.");
			ra.addFlashAttribute("messageSuccess", "The section ID " + id + " has been moved up by one position.");

		} catch (SectionUnmoveableException | SectionNotFoundException ex) {
			LOGGER.info("GeneralSectionController | moveSectionUp | messageError : " + ex.getMessage());
			ra.addFlashAttribute("messageError", ex.getMessage());
		}

		return "redirect:/sections";		
	}

	@GetMapping("/sections/movedown/{id}")
	public String moveSectionDown(@PathVariable(name = "id") Integer id, RedirectAttributes ra) {
		
		LOGGER.info("GeneralSectionController | moveSectionDown is called");
		
		try {
			service.moveSectionDown(id);
			
			LOGGER.info("GeneralSectionController | moveSectionDown | messageSuccess : " + "The section ID " + id + " has been moved down by one position.");
			ra.addFlashAttribute("messageSuccess", "The section ID " + id + " has been moved down by one position.");

		} catch (SectionUnmoveableException | SectionNotFoundException ex) {
			LOGGER.info("GeneralSectionController | moveSectionDown | messageError : " + ex.getMessage());
			ra.addFlashAttribute("messageError", ex.getMessage());
		}

		return "redirect:/sections";		
	}	
}
