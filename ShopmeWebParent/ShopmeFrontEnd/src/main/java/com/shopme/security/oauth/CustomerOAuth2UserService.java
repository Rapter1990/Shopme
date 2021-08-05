package com.shopme.security.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomerOAuth2UserService extends DefaultOAuth2UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerOAuth2UserService.class);
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		String clientName = userRequest.getClientRegistration().getClientName();
		OAuth2User user = super.loadUser(userRequest);
		
		LOGGER.info("CustomerOAuth2UserService | loadUser |  clientName : " + clientName);
		LOGGER.info("CustomerOAuth2UserService | loadUser |  name : " + user.toString());
		
		return new CustomerOAuth2User(user, clientName);
	}

}
