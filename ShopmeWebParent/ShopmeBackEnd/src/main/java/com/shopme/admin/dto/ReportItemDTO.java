package com.shopme.admin.dto;

import java.util.Objects;

public class ReportItemDTO {

	private String identifier;
	private float grossSales;
	private float netSales;
	private int ordersCount;
	private int productsCount;

	public ReportItemDTO() {
	}

	public ReportItemDTO(String identifier) {
		this.identifier = identifier;
	}

	public ReportItemDTO(String identifier, float grossSales, float netSales) {
		this.identifier = identifier;
		this.grossSales = grossSales;
		this.netSales = netSales;
	}
	
	public ReportItemDTO(String identifier, float grossSales, float netSales, int productsCount) {
		super();
		this.identifier = identifier;
		this.grossSales = grossSales;
		this.netSales = netSales;
		this.productsCount = productsCount;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public float getGrossSales() {
		return grossSales;
	}

	public void setGrossSales(float grossSales) {
		this.grossSales = grossSales;
	}

	public float getNetSales() {
		return netSales;
	}

	public void setNetSales(float netSales) {
		this.netSales = netSales;
	}

	public int getOrdersCount() {
		return ordersCount;
	}

	public void setOrdersCount(int ordersCount) {
		this.ordersCount = ordersCount;
	}
	
	public int getProductsCount() {
		return productsCount;
	}

	public void setProductsCount(int productsCount) {
		this.productsCount = productsCount;
	}

	public void increaseProductsCount(int count) {
		this.productsCount += count;
	}

	@Override
	public int hashCode() {
		return Objects.hash(identifier);
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportItemDTO other = (ReportItemDTO) obj;
		return Objects.equals(identifier, other.identifier);
	}

	public void addGrossSales(float amount) {
		this.grossSales += amount;

	}

	public void addNetSales(float amount) {
		this.netSales += amount;
	}

	public void increaseOrdersCount() {
		this.ordersCount++;
	}

	@Override
	public String toString() {
		return "ReportItemDTO [identifier=" + identifier + ", grossSales=" + grossSales + ", netSales=" + netSales
				+ ", ordersCount=" + ordersCount + "]";
	}
	
}
