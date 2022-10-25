package com.shopme.admin.service;

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
		Query query = entityManager.createQuery("(SELECT COUNT(DISTINCT u.id) AS totalUsers FROM User u)");
		
		return summary;
	}
}
