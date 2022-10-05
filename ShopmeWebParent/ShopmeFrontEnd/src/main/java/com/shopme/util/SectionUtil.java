package com.shopme.util;

import java.util.List;

import com.shopme.common.entity.section.Section;
import com.shopme.common.entity.section.SectionType;

public class SectionUtil {

	public static boolean hasAllCategoriesSection(List<Section> listSections) {
		// TODO Auto-generated method stub
		for (Section section : listSections) {
			if (section.getType().equals(SectionType.ALL_CATEGORIES)) {
				return true;
			}
		}

		return false;
	}

}
