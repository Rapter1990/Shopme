package com.shopme.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.checkout.CheckoutInfo;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.order.PaymentMethod;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.service.AddressService;
import com.shopme.service.CheckoutService;
import com.shopme.service.CustomerService;
import com.shopme.service.OrderService;
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
	private OrderService orderService;
	
	@Autowired
	public CheckoutController(CheckoutService checkoutService, CustomerService customerService,
			AddressService addressService, ShippingRateService shipService, ShoppingCartService cartService,
			OrderService orderService) {
		super();
		this.checkoutService = checkoutService;
		this.customerService = customerService;
		this.addressService = addressService;
		this.shipService = shipService;
		this.cartService = cartService;
		this.orderService = orderService;
	}

	@GetMapping("/checkout")
	public String showCheckoutPage(Model model, HttpServletRequest request) {
		
		LOGGER.info("CheckoutController | showCheckoutPage is called");
		
		Customer customer = null;
		try {
			customer = CustomerShoppingCartAddressShippingUtil.getAuthenticatedCustomer(request,customerService);
		} catch (CustomerNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LOGGER.info("CheckoutController | showCheckoutPage | customer : " + customer.toString());

		Address defaultAddress = addressService.getDefaultAddress(customer);
		
		ShippingRate shippingRate = null;

		if (defaultAddress != null) {
			model.addAttribute("shippingAddress", defaultAddress.toString());
			shippingRate = shipService.getShippingRateForAddress(defaultAddress);
			
			LOGGER.info("CheckoutController | showCheckoutPage | defaultAddress != null | shippingRate " + shippingRate.toString());
			
		} else {
			model.addAttribute("shippingAddress", customer.toString());
			shippingRate = shipService.getShippingRateForCustomer(customer);
			
			LOGGER.info("CheckoutController | showCheckoutPage | defaultAddress == null | shippingRate " + shippingRate.toString());
		}
		
		LOGGER.info("CheckoutController | showCheckoutPage | shippingRate " + shippingRate.toString());

		if (shippingRate == null) {
			LOGGER.info("CheckoutController | showCheckoutPage | \"redirect:/cart\" ");
			return "redirect:/cart";
		}

		List<CartItem> cartItems = cartService.listCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);
		
		LOGGER.info("CheckoutController | showCheckoutPage | cartItems " + cartItems.toString());
		LOGGER.info("CheckoutController | showCheckoutPage | checkoutInfo " + checkoutInfo.toString());

		model.addAttribute("checkoutInfo", checkoutInfo);
		model.addAttribute("cartItems", cartItems);

		return "checkout/checkout";
	}
	
	@PostMapping("/place_order")
	public String placeOrder(HttpServletRequest request) {
		
		LOGGER.info("CheckoutController | placeOrder is called");
		
		String paymentType = request.getParameter("paymentMethod");
		
		LOGGER.info("CheckoutController | placeOrder | paymentType :  " + paymentType);
		
		PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);
		
		LOGGER.info("CheckoutController | placeOrder | paymentMethod :  " + paymentMethod.toString());
	
		Customer customer = null;;
		try {
			customer = CustomerShoppingCartAddressShippingUtil.getAuthenticatedCustomer(request,customerService);
		} catch (CustomerNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LOGGER.info("CheckoutController | placeOrder | customer :  " + customer.toString());

		Address defaultAddress = addressService.getDefaultAddress(customer);
		ShippingRate shippingRate = null;
		
		LOGGER.info("CheckoutController | placeOrder | defaultAddress != null :  " + (defaultAddress != null));

		if (defaultAddress != null) {
			shippingRate = shipService.getShippingRateForAddress(defaultAddress);
			
			LOGGER.info("CheckoutController | placeOrder | shippingRate :  " + shippingRate.toString());
			
		} else {
			shippingRate = shipService.getShippingRateForCustomer(customer);
			
			LOGGER.info("CheckoutController | placeOrder | shippingRate :  " + shippingRate.toString());
		}
		
		List<CartItem> cartItems = cartService.listCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);
		
		LOGGER.info("CheckoutController | placeOrder | cartItems " + cartItems.toString());
		LOGGER.info("CheckoutController | placeOrder | checkoutInfo " + checkoutInfo.toString());

		orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);
		cartService.deleteByCustomer(customer);

		return "checkout/order_completed";
	}
}
