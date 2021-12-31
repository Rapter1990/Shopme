package com.shopme.util;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.shopme.common.entity.Customer;
import com.shopme.security.CustomerUserDetails;
import com.shopme.security.oauth.CustomerOAuth2User;

public class CustomerAccountUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAccountUtil.class);

	public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
		
		LOGGER.info("CustomerAccountUtil | getEmailOfAuthenticatedCustomer is called");
		
		Object principal = request.getUserPrincipal();
		
		if(principal != null) {
			LOGGER.info("CustomerAccountUtil | getEmailOfAuthenticatedCustomer | principal : " + principal.toString());
		}else {
			LOGGER.info("CustomerAccountUtil | getEmailOfAuthenticatedCustomer | principal = null ");
		}
		
		
		String customerEmail = null;
		
		
		LOGGER.info("CustomerAccountUtil | getEmailOfAuthenticatedCustomer | "
				+ "principal instanceof UsernamePasswordAuthenticationToken  : " + (principal instanceof UsernamePasswordAuthenticationToken));

		LOGGER.info("CustomerAccountUtil | getEmailOfAuthenticatedCustomer | "
				+ "principal instanceof RememberMeAuthenticationToken  : " + (principal instanceof RememberMeAuthenticationToken));
		
		LOGGER.info("CustomerAccountUtil | getEmailOfAuthenticatedCustomer | "
				+ "principal instanceof OAuth2AuthenticationToken  : " + (principal instanceof OAuth2AuthenticationToken));
		
		if (principal instanceof UsernamePasswordAuthenticationToken 
				|| principal instanceof RememberMeAuthenticationToken) {
			customerEmail = request.getUserPrincipal().getName();
		} else if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
			
			LOGGER.info("CustomerAccountUtil | getEmailOfAuthenticatedCustomer | oauth2User : " + oauth2User.toString());
			
			customerEmail = oauth2User.getEmail();
		}
		
		LOGGER.info("CustomerAccountUtil | getEmailOfAuthenticatedCustomer | customerEmail : " + customerEmail);

		return customerEmail;
	}
	
	public static void updateNameForAuthenticatedCustomer(Customer customer, HttpServletRequest request) {
		
		LOGGER.info("CustomerAccountUtil | updateNameForAuthenticatedCustomer is called");
		
		Object principal = request.getUserPrincipal();
		
		
		LOGGER.info("CustomerAccountUtil | updateNameForAuthenticatedCustomer | principal : " + principal.toString());
		
		LOGGER.info("CustomerAccountUtil | updateNameForAuthenticatedCustomer | "
				+ "principal instanceof UsernamePasswordAuthenticationToken  : " + (principal instanceof UsernamePasswordAuthenticationToken));

		LOGGER.info("CustomerAccountUtil | updateNameForAuthenticatedCustomer | "
				+ "principal instanceof RememberMeAuthenticationToken  : " + (principal instanceof RememberMeAuthenticationToken));
		
		LOGGER.info("CustomerAccountUtil | updateNameForAuthenticatedCustomer | "
				+ "principal instanceof OAuth2AuthenticationToken  : " + (principal instanceof OAuth2AuthenticationToken));

		if (principal instanceof UsernamePasswordAuthenticationToken 
				|| principal instanceof RememberMeAuthenticationToken) {
			
			CustomerUserDetails userDetails = getCustomerUserDetailsObject(principal);
			
			LOGGER.info("CustomerAccountUtil | updateNameForAuthenticatedCustomer | userDetails : " + userDetails.toString());
			
			Customer authenticatedCustomer = userDetails.getCustomer();
			
			LOGGER.info("CustomerAccountUtil | updateNameForAuthenticatedCustomer | authenticatedCustomer : " + authenticatedCustomer.toString());
			
			authenticatedCustomer.setFirstName(customer.getFirstName());
			authenticatedCustomer.setLastName(customer.getLastName());

		} else if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
			
			LOGGER.info("CustomerAccountUtil | updateNameForAuthenticatedCustomer | oauth2User : " + oauth2User.toString());
			
			String fullName = customer.getFirstName() + " " + customer.getLastName();
			
			LOGGER.info("CustomerAccountUtil | updateNameForAuthenticatedCustomer | fullName : " + fullName);
			
			oauth2User.setFullName(fullName);
		}		
	}

	public static CustomerUserDetails getCustomerUserDetailsObject(Object principal) {
		
		LOGGER.info("CustomerAccountUtil | getCustomerUserDetailsObject is called");
		
		CustomerUserDetails userDetails = null;
		
		LOGGER.info("CustomerAccountUtil | getCustomerUserDetailsObject | "
				+ "principal instanceof UsernamePasswordAuthenticationToken  : " + (principal instanceof UsernamePasswordAuthenticationToken));

		LOGGER.info("CustomerAccountUtil | getCustomerUserDetailsObject | "
				+ "principal instanceof RememberMeAuthenticationToken  : " + (principal instanceof RememberMeAuthenticationToken));
		
		if (principal instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal();
		} else if (principal instanceof RememberMeAuthenticationToken) {
			RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal();
		}

		LOGGER.info("CustomerAccountUtil | getCustomerUserDetailsObject | userDetails : " + userDetails.toString());
		
		return userDetails;
	}
}
