package com.shopme.admin.util;

import javax.servlet.http.HttpServletRequest;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.article.Article;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.section.ArticleSection;
import com.shopme.common.entity.section.BrandSection;
import com.shopme.common.entity.section.CategorySection;
import com.shopme.common.entity.section.ProductSection;
import com.shopme.common.entity.section.Section;

public class SectionUtil {

	public static void addArticlesToSection(Section section, HttpServletRequest request) {
		String[] IDs = request.getParameterValues("chosenArticles");

		if (IDs != null && IDs.length > 0) {
			for (int i = 0; i < IDs.length; i++) {
				String[] arrayIds = IDs[i].split("-");

				ArticleSection articleSection = new ArticleSection();

				Integer articleSectionId = Integer.valueOf(arrayIds[1]);
				if (articleSectionId > 0) {
					articleSection.setId(articleSectionId);
				}

				articleSection.setArticleOrder(i);
				Integer articleId = Integer.valueOf(arrayIds[0]);

				articleSection.setArticle(new Article(articleId));
				section.addArticleSection(articleSection);

			}
		}		
	}
	
	public static void addBrandsToSection(Section section, HttpServletRequest request) {
		String[] IDs = request.getParameterValues("chosenBrands");

		if (IDs != null && IDs.length > 0) {
			for (int i = 0; i < IDs.length; i++) {
				String[] arrayIds = IDs[i].split("-");

				BrandSection brandSection = new BrandSection();

				Integer brandSectionId = Integer.valueOf(arrayIds[1]);
				if (brandSectionId > 0) {
					brandSection.setId(brandSectionId);
				}

				brandSection.setBrandOrder(i);
				Integer brandId = Integer.valueOf(arrayIds[0]);

				brandSection.setBrand(new Brand(brandId));
				section.addBrandSection(brandSection);

			}
		}		
	}
	
	public static void addCategoriesToSection(Section section, HttpServletRequest request) {
		String[] IDs = request.getParameterValues("chosenCategories");

		if (IDs != null && IDs.length > 0) {
			for (int i = 0; i < IDs.length; i++) {
				String[] arrayIds = IDs[i].split("-");

				CategorySection categorySection = new CategorySection();

				Integer categorySectionId = Integer.valueOf(arrayIds[1]);
				if (categorySectionId > 0) {
						categorySection.setId(categorySectionId);
				}

				categorySection.setCategoryOrder(i);
				Integer categoryId = Integer.valueOf(arrayIds[0]);

				categorySection.setCategory(new Category(categoryId));
				section.addCategorySection(categorySection);

			}
		}		
	}
	
	public static void addProductsToSection(Section section, HttpServletRequest request) {
		String[] productIds = request.getParameterValues("productId");
		String[] productSectionIds = request.getParameterValues("productSectionId");

		if (productIds != null && productIds.length > 0) {
			for (int i = 0; i < productIds.length; i++) {
				ProductSection productSection = new ProductSection();

				if (productSectionIds != null && productSectionIds.length > 0) {
					if (i < productSectionIds.length) {
						Integer productSectionId = Integer.valueOf(productSectionIds[i]);
						productSection.setId(productSectionId);
					}
				}

				productSection.setProductOrder(i);
				Integer productId = Integer.valueOf(productIds[i]);
				productSection.setProduct(new Product(productId));

				section.addProductSection(productSection);
			}
		}
	}
}
