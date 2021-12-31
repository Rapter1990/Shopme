package com.shopme.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ShoppingCartException;
import com.shopme.service.CustomerService;
import com.shopme.service.ShoppingCartService;
import com.shopme.util.AuthenticationControllerHelperUtil;

@RestController
public class ShoppingCartRestController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartRestController.class);
	
	private ShoppingCartService cartService;
	
	private AuthenticationControllerHelperUtil authenticationControllerHelperUtil;
	
	@Autowired
	public ShoppingCartRestController(ShoppingCartService cartService,
			CustomerService customerService,
			AuthenticationControllerHelperUtil authenticationControllerHelperUtil) {
		super();
		this.cartService = cartService;
		this.authenticationControllerHelperUtil = authenticationControllerHelperUtil;
	}


	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {
		
		LOGGER.info("ShoppingCartRestController | addProductToCart is called");

		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		if(customer != null) {
			LOGGER.info("ShoppingCartRestController | addProductToCart | customer : " + customer.toString());
			
			Integer updatedQuantity;
			try {
				updatedQuantity = cartService.addProduct(productId, quantity, customer);
			} catch (ShoppingCartException ex) {
				// TODO Auto-generated catch block
				return ex.getMessage();
			}
			
			LOGGER.info("ShoppingCartRestController | addProductToCart | updatedQuantity : " + updatedQuantity);
			
			return updatedQuantity + " item(s) of this product were added to your shopping cart.";
		}else {
			return "You must login to add this product to cart.";
		}
		
	}

	
	@DeleteMapping("/cart/remove/{productId}")
	public String removeProduct(@PathVariable("productId") Integer productId,
			HttpServletRequest request) {
		
		LOGGER.info("ShoppingCartRestController | removeProduct is called");
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		if(customer != null) {
			
			LOGGER.info("ShoppingCartRestController | removeProduct | customer : " + customer.toString());
			
			cartService.removeProduct(productId, customer);
			
			return "The product has been removed from your shopping cart.";
		}else {
			return "You must login to remove product.";
		}
		
	}
	
	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {
		
		LOGGER.info("ShoppingCartRestController | updateQuantity is called");
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		if(customer != null) {
			LOGGER.info("ShoppingCartRestController | updateQuantity | customer : " + customer.toString());
			
			float subtotal = cartService.updateQuantity(productId, quantity, customer);
			
			LOGGER.info("ShoppingCartRestController | updateQuantity | subtotal : " + subtotal);

			return String.valueOf(subtotal);
		}else {
			return "You must login to change quantity of product.";
		}
		
	}
}
