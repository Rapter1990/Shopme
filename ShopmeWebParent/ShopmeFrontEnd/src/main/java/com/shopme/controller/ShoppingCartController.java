package com.shopme.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.service.CustomerService;
import com.shopme.service.ShoppingCartService;
import com.shopme.util.CustomerShoppingCartUtil;

@Controller
public class ShoppingCartController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartController.class);
	
	private CustomerService customerService;
	
	private ShoppingCartService cartService;
	
	@Autowired
	public ShoppingCartController(CustomerService customerService, ShoppingCartService cartService) {
		super();
		this.customerService = customerService;
		this.cartService = cartService;
	}

	@GetMapping("/cart")
	public String viewCart(Model model, HttpServletRequest request) throws CustomerNotFoundException {
		
		LOGGER.info("ShoppingCartController | viewCart is called");
		
		Customer customer = CustomerShoppingCartUtil.getAuthenticatedCustomer(request,customerService);
		List<CartItem> cartItems = cartService.listCartItems(customer);
		
		LOGGER.info("ShoppingCartController | viewCart | customer : " + customer.toString());
		LOGGER.info("ShoppingCartController | viewCart | cartItems : " + cartItems.size());

		float estimatedTotal = 0.0F;

		for (CartItem item : cartItems) {
			LOGGER.info("ShoppingCartController | viewCart | item.getSubtotal() : " + item.getSubtotal());
			estimatedTotal += item.getSubtotal();
		}

		model.addAttribute("cartItems", cartItems);
		model.addAttribute("estimatedTotal", estimatedTotal);
		
		LOGGER.info("ShoppingCartController | viewCart | estimatedTotal : " + estimatedTotal);

		return "cart/shopping_cart";
	}

}