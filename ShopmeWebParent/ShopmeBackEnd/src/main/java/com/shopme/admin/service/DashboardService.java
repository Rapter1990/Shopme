package com.shopme.admin.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.dashboard.DashboardInfo;

@Service
public class DashboardService {

	@Autowired
	private EntityManager entityManager;
	
	public DashboardInfo loadSummary() {
		
		DashboardInfo summary = new DashboardInfo();
		
		Query query = entityManager.createQuery("SELECT "
				+ "(SELECT COUNT(DISTINCT u.id) AS totalUsers FROM User u),"
				+ "(SELECT COUNT(DISTINCT c.id) AS totalCategories FROM Category c), "
				+ "(SELECT COUNT(DISTINCT b.id) AS totalBrands FROM Brand b), "
				+ "(SELECT COUNT(DISTINCT p.id) AS totalProducts FROM Product p), "
				+ "(SELECT COUNT(DISTINCT q.id) AS totalQuestions FROM Question q), "
				+ "(SELECT COUNT(DISTINCT r.id) AS totalReviews FROM Review r), "
				+ "(SELECT COUNT(DISTINCT cu.id) AS totalCustomers FROM Customer cu), "
				+ "(SELECT COUNT(DISTINCT o.id) AS totalOrders FROM Order o), "
				+ "(SELECT COUNT(DISTINCT sr.id) AS totalShippingRates FROM ShippingRate sr), "
				+ "(SELECT COUNT(DISTINCT a.id) AS totalArticles FROM Article a), "
				+ "(SELECT COUNT(DISTINCT m.id) AS totalMenuItems FROM Menu m), "
				+ "(SELECT COUNT(DISTINCT se.id) AS totalSections FROM Section se), "
				+ "(SELECT COUNT(DISTINCT u.id) AS disabledUsers FROM User u WHERE u.enabled=false), "
				+ "(SELECT COUNT(DISTINCT u.id) AS enabledUsers FROM User u WHERE u.enabled=true), "
				+ "(SELECT COUNT(DISTINCT c.id) AS rootCategories FROM Category c WHERE c.parent is null), "
				+ "(SELECT COUNT(DISTINCT c.id) AS enabledCategories FROM Category c WHERE c.enabled=true), "
				+ "(SELECT COUNT(DISTINCT c.id) AS disabledCategories FROM Category c WHERE c.enabled=false), "
				+ "(SELECT COUNT(DISTINCT p.id) AS enabledProducts FROM Product p WHERE p.enabled=true), "
				+ "(SELECT COUNT(DISTINCT p.id) AS disabledProducts FROM Product p WHERE p.enabled=false), "
				+ "(SELECT COUNT(DISTINCT p.id) AS inStockProducts FROM Product p WHERE p.inStock=true), "
				+ "(SELECT COUNT(DISTINCT p.id) AS outOfStockProducts FROM Product p WHERE p.inStock=false), "
				+ "st.value as siteName,"
				+ "FROM Setting st WHERE st.key='site_name'"
				);
		
		List<Object[]> entityCounts = query.getResultList();
		Object[] arrayCounts = entityCounts.get(0);
		
		int count = 0;
		
		summary.setTotalUsers((Long) arrayCounts[count++]);
		summary.setTotalCategories((Long) arrayCounts[count++]);
		summary.setTotalBrands((Long) arrayCounts[count++]);
		summary.setTotalProducts((Long) arrayCounts[count++]);
		summary.setTotalQuestions((Long) arrayCounts[count++]);
		summary.setTotalReviews((Long) arrayCounts[count++]);
		summary.setTotalCustomers((Long) arrayCounts[count++]);
		summary.setTotalOrders((Long) arrayCounts[count++]);
		summary.setTotalShippingRates((Long) arrayCounts[count++]);
		summary.setTotalArticles((Long) arrayCounts[count++]);
		summary.setTotalMenuItems((Long) arrayCounts[count++]);
		summary.setTotalSections((Long) arrayCounts[count++]);
		
		summary.setDisabledUsersCount((Long) arrayCounts[count++]);
		summary.setEnabledUsersCount((Long) arrayCounts[count++]);
		
		summary.setRootCategoriesCount((Long) arrayCounts[count++]);
		summary.setEnabledCategoriesCount((Long) arrayCounts[count++]);
		summary.setDisabledCategoriesCount((Long) arrayCounts[count++]);
		
		summary.setEnabledProductsCount((Long) arrayCounts[count++]);
		summary.setDisabledProductsCount((Long) arrayCounts[count++]);
		
		
		summary.setInStockProductsCount((Long) arrayCounts[count++]);
		summary.setOutOfStockProductsCount((Long) arrayCounts[count++]);
				
		return summary;
	}
}
