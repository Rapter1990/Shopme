package com.shopme.service.impl;

import java.util.List;

import com.shopme.checkout.CheckoutInfo;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.ShippingRate;

public interface ICheckoutService {
	public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate);
}
