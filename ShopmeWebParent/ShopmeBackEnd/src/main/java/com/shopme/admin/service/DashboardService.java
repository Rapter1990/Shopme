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
				+ "st.value as siteName,"
				+ "FROM Setting st WHERE st.key='site_name'"
				);
		
		List<Object[]> entityCounts = query.getResultList();
		Object[] arrayCounts = entityCounts.get(0);
		
		int count = 0;
		
		summary.setTotalUsers((Long) arrayCounts[count++]);
		summary.setTotalCategories((Long) arrayCounts[count++]);
		summary.setTotalBrands((Long) arrayCounts[count++]);
				
		return summary;
	}
}
