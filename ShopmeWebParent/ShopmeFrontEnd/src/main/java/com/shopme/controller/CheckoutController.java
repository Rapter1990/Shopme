package com.shopme.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.checkout.CheckoutInfo;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.service.AddressService;
import com.shopme.service.CheckoutService;
import com.shopme.service.CustomerService;
import com.shopme.service.ShippingRateService;
import com.shopme.service.ShoppingCartService;
import com.shopme.util.CustomerShoppingCartAddressShippingUtil;

@Controller
public class CheckoutController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutController.class);
	
	private CheckoutService checkoutService;
	private CustomerService customerService;
	private AddressService addressService;
	private ShippingRateService shipService;
	private ShoppingCartService cartService;
	
	@Autowired
	public CheckoutController(CheckoutService checkoutService, CustomerService customerService,
			AddressService addressService, ShippingRateService shipService, ShoppingCartService cartService) {
		super();
		this.checkoutService = checkoutService;
		this.customerService = customerService;
		this.addressService = addressService;
		this.shipService = shipService;
		this.cartService = cartService;
	}

	@GetMapping("/checkout")
	public String showCheckoutPage(Model model, HttpServletRequest request) {
		
		LOGGER.info("CustomerController | showCheckoutPage is called");
		
		Customer customer = null;
		try {
			customer = CustomerShoppingCartAddressShippingUtil.getAuthenticatedCustomer(request,customerService);
		} catch (CustomerNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LOGGER.info("CustomerController | showCheckoutPage | customer : " + customer.toString());

		Address defaultAddress = addressService.getDefaultAddress(customer);
		
		ShippingRate shippingRate = null;

		if (defaultAddress != null) {
			model.addAttribute("shippingAddress", defaultAddress.toString());
			shippingRate = shipService.getShippingRateForAddress(defaultAddress);
			
			LOGGER.info("CustomerController | showCheckoutPage | defaultAddress != null | shippingRate " + shippingRate.toString());
			
		} else {
			model.addAttribute("shippingAddress", customer.toString());
			shippingRate = shipService.getShippingRateForCustomer(customer);
			
			LOGGER.info("CustomerController | showCheckoutPage | defaultAddress == null | shippingRate " + shippingRate.toString());
		}
		
		LOGGER.info("CustomerController | showCheckoutPage | shippingRate " + shippingRate.toString());

		if (shippingRate == null) {
			LOGGER.info("CustomerController | showCheckoutPage | \"redirect:/cart\" ");
			return "redirect:/cart";
		}

		List<CartItem> cartItems = cartService.listCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);
		
		LOGGER.info("CustomerController | showCheckoutPage | cartItems " + cartItems.toString());
		LOGGER.info("CustomerController | showCheckoutPage | checkoutInfo " + checkoutInfo.toString());

		model.addAttribute("checkoutInfo", checkoutInfo);
		model.addAttribute("cartItems", cartItems);

		return "checkout/checkout";
	}
}
