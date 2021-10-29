package com.shopme.util;

public class OrderReturnResponse {
	
	private Integer orderId;

	public OrderReturnResponse() { }

	public OrderReturnResponse(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
}
