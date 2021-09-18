package com.shopme.admin.service.impl;

import com.shopme.admin.error.OrderNotFoundException;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.order.Order;

public interface IOrderService {

	public void listByPage(int pageNum, PagingAndSortingHelper helper);

	public Order get(Integer id) throws OrderNotFoundException;

	public void delete(Integer id) throws OrderNotFoundException;
}
