package com.shopme.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.shopme.common.constants.Constants;
import com.shopme.common.entity.setting.Setting;
import com.shopme.service.SettingService;

@Component
@Order(-121) // use this value to fix Logout Error of Customer already signed out
// Default value -100 (https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html -> spring.security.filter.order)
// Select any value less than default value
public class SettingFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SettingFilter.class);
	
	@Autowired
	private SettingService service; 

	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		LOGGER.info("SettingFilter | doFilter is called");
		
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		String url = servletRequest.getRequestURL().toString();
		
		LOGGER.info("SettingFilter | doFilter | url : " + url);

		if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png") ||
				url.endsWith(".jpg")) {
			LOGGER.info("SettingFilter | doFilter | .css , .js , .png , . jpg | url : " + url);
			chain.doFilter(request, response);
			return;
		}

		List<Setting> generalSettings = service.getGeneralSettings();

		generalSettings.forEach(setting -> {
			LOGGER.info("SettingFilter | doFilter | generalSettings : " + generalSettings);
			request.setAttribute(setting.getKey(), setting.getValue());
		});
		
		request.setAttribute("S3_BASE_URI", Constants.S3_BASE_URI);
		
		LOGGER.info("SettingFilter | doFilter | S3_BASE_URI : " + Constants.S3_BASE_URI);

		chain.doFilter(request, response);

	}

}
