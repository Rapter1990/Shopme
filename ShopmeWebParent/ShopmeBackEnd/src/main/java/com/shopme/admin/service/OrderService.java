package com.shopme.admin.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.error.OrderNotFoundException;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.repository.OrderRepository;
import com.shopme.admin.service.impl.IOrderService;
import com.shopme.common.entity.order.Order;

@Service
public class OrderService implements IOrderService{

	private static final int ORDERS_PER_PAGE = 10;

	@Autowired 
	private OrderRepository repo;

	@Override
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		
		String sortField = helper.getSortField();
		String sortDir = helper.getSortDir();
		String keyword = helper.getKeyword();

		Sort sort = null;

		if ("destination".equals(sortField)) {
			sort = Sort.by("country").and(Sort.by("state")).and(Sort.by("city"));
		} else {
			sort = Sort.by(sortField);
		}

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);

		Page<Order> page = null;

		if (keyword != null) {
			page = repo.findAll(keyword, pageable);
		} else {
			page = repo.findAll(pageable);
		}

		helper.updateModelAttributes(pageNum, page);		
	}
	
	@Override
	public Order get(Integer id) throws OrderNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new OrderNotFoundException("Could not find any orders with ID " + id);
		}
	}
	
	@Override
	public void delete(Integer id) throws OrderNotFoundException {
		Long count = repo.countById(id);
		if (count == null || count == 0) {
			throw new OrderNotFoundException("Could not find any orders with ID " + id); 
		}

		repo.deleteById(id);
	}	
}
