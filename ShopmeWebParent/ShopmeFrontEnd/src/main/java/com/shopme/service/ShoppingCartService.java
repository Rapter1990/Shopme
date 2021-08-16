package com.shopme.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ShoppingCartException;
import com.shopme.repository.CartItemRepository;
import com.shopme.service.impl.IShoppingCartService;

@Service
public class ShoppingCartService implements IShoppingCartService{

	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartService.class);
	
	@Autowired 
	private CartItemRepository cartRepo;
	
	@Override
	public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {
		// TODO Auto-generated method stub
		
		LOGGER.info("ShoppingCartService | addProduct is called");
		
		Integer updatedQuantity = quantity;
		
		LOGGER.info("ShoppingCartService | addProduct | updatedQuantity(at first) : " + updatedQuantity);
		
		Product product = new Product(productId);
		
		LOGGER.info("ShoppingCartService | addProduct | product : " + product.toString());

		CartItem cartItem = cartRepo.findByCustomerAndProduct(customer, product);
		
		LOGGER.info("ShoppingCartService | addProduct | cartItem : " + cartItem.toString());
		
		LOGGER.info("ShoppingCartService | addProduct | cartItem != null : " + (cartItem != null));

		if (cartItem != null) {
			
			updatedQuantity = cartItem.getQuantity() + quantity;
			
			LOGGER.info("ShoppingCartService | addProduct | updatedQuantity(updated) : " + updatedQuantity);

			if (updatedQuantity > 5) {
				
				LOGGER.info("ShoppingCartService | addProduct | updatedQuantity > 5 : " + (updatedQuantity > 5));
				
				throw new ShoppingCartException("Could not add more " + quantity + " item(s)"
						+ " because there's already " + cartItem.getQuantity() + " item(s) "
						+ "in your shopping cart. Maximum allowed quantity is 5.");
			}
		} else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
		}

		LOGGER.info("ShoppingCartService | addProduct | updatedQuantity(at last) : " + updatedQuantity);
		
		cartItem.setQuantity(updatedQuantity);
		
		LOGGER.info("ShoppingCartService | addProduct | cartItem : " + cartItem.toString());

		cartRepo.save(cartItem);

		return updatedQuantity;
	}

	@Override
	public List<CartItem> listCartItems(Customer customer) {
		// TODO Auto-generated method stub
		
		LOGGER.info("ShoppingCartService | listCartItems is called");
		
		LOGGER.info("ShoppingCartService | listCartItems | listCartItems size : " + cartRepo.findByCustomer(customer).size());
		
		return cartRepo.findByCustomer(customer);
	}

}
