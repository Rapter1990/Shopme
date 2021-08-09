package com.shopme.security.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomerOAuth2User implements OAuth2User {

	private String clientName;
	private OAuth2User oauth2User;
	private String fullName;

	public CustomerOAuth2User(OAuth2User user, String clientName) {
		this.oauth2User = user;
		this.clientName = clientName;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oauth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return oauth2User.getAuthorities();
	}

	@Override
	public String getName() {
		return oauth2User.getAttribute("name");
	}

	public String getEmail() {
		return oauth2User.getAttribute("email");
	}

	public String getFullName() {
		return fullName != null ? fullName : oauth2User.getAttribute("name");
	}
	
	public String getClientName() {
		return clientName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}