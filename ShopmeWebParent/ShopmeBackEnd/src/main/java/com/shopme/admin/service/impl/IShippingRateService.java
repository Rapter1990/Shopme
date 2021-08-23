package com.shopme.admin.service.impl;

import java.util.List;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.ShippingRateAlreadyExistsException;
import com.shopme.common.exception.ShippingRateNotFoundException;

public interface IShippingRateService {

	public void listByPage(int pageNum, PagingAndSortingHelper helper);
	public List<Country> listAllCountries();
	public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException;
	public ShippingRate get(Integer id) throws ShippingRateNotFoundException;
	public void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateNotFoundException;
	public void delete(Integer id) throws ShippingRateNotFoundException;
	
}
