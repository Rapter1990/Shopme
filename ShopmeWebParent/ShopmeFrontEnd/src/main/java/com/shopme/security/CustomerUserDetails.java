package com.shopme.security;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shopme.common.entity.Customer;

public class CustomerUserDetails implements UserDetails{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerUserDetails.class);
	
	private static final long serialVersionUID = 1L;
	
	private Customer customer;

	public CustomerUserDetails(Customer customer) {
		
		LOGGER.info("CustomerUserDetails | customer: " + customer.toString());
		
		this.customer = customer;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		
		LOGGER.info("CustomerUserDetails | password: " + customer.getPassword());
		
		return customer.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		
		LOGGER.info("CustomerUserDetails | username: " + customer.getEmail());
		
		return customer.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		
		LOGGER.info("CustomerUserDetails | isEnabled: " + customer.isEnabled());
		
		return customer.isEnabled();
	}
	
	public String getFullName() {
		
		LOGGER.info("CustomerUserDetails | getFullName: " + customer.getFirstName() + " " + customer.getLastName());
		
		return customer.getFirstName() + " " + customer.getLastName();
	}
	
	public Customer getCustomer() {
		return this.customer;
	}

}
