package com.shopme.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.service.AddressService;
import com.shopme.service.CustomerService;
import com.shopme.service.ShippingRateService;
import com.shopme.service.ShoppingCartService;
import com.shopme.util.AuthenticationControllerHelperUtil;

@Controller
public class ShoppingCartController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartController.class);
	
	private ShoppingCartService cartService;
	
	private AddressService addressService;
	
	private ShippingRateService shipService;
	
	private AuthenticationControllerHelperUtil authenticationControllerHelperUtil;
	
	@Autowired
	public ShoppingCartController(CustomerService customerService, ShoppingCartService cartService,
			AddressService addressService, ShippingRateService shipService,
			AuthenticationControllerHelperUtil authenticationControllerHelperUtil) {
		
		super();
		this.cartService = cartService;
		this.addressService = addressService;
		this.shipService = shipService;
		this.authenticationControllerHelperUtil = authenticationControllerHelperUtil;
	}

	@GetMapping("/cart")
	public String viewCart(Model model, HttpServletRequest request) throws CustomerNotFoundException {
		
		LOGGER.info("ShoppingCartController | viewCart is called");
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		List<CartItem> cartItems = cartService.listCartItems(customer);
		
		LOGGER.info("ShoppingCartController | viewCart | customer : " + customer.toString());
		LOGGER.info("ShoppingCartController | viewCart | cartItems : " + cartItems.size());

		float estimatedTotal = 0.0F;

		for (CartItem item : cartItems) {
			LOGGER.info("ShoppingCartController | viewCart | item.getSubtotal() : " + item.getSubtotal());
			estimatedTotal += item.getSubtotal();
		}
		
		Address defaultAddress = addressService.getDefaultAddress(customer);
		
		ShippingRate shippingRate = null;
		boolean usePrimaryAddressAsDefault = false;

		if (defaultAddress != null) {
			shippingRate = shipService.getShippingRateForAddress(defaultAddress);
		} else {
			usePrimaryAddressAsDefault = true;
			shippingRate = shipService.getShippingRateForCustomer(customer);
		}

		LOGGER.info("ShoppingCartController | viewCart | usePrimaryAddressAsDefault : " + usePrimaryAddressAsDefault);
		LOGGER.info("ShoppingCartController | viewCart | shippingSupported : " + (shippingRate != null));
		
		model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
		model.addAttribute("shippingSupported", shippingRate != null);

		model.addAttribute("cartItems", cartItems);
		model.addAttribute("estimatedTotal", estimatedTotal);
		
		LOGGER.info("ShoppingCartController | viewCart | estimatedTotal : " + estimatedTotal);

		return "cart/shopping_cart";
	}
	

}