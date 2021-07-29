package com.shopme.util;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.EmailSettingBag;
import com.shopme.service.SettingService;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomerRegisterUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRegisterUtil.class);

	public static void encodePassword(Customer customer, PasswordEncoder passwordEncoder) {
		
		LOGGER.info("CustomerRegisterUtil | encodePassword is called");
		
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		
		LOGGER.info("CustomerRegisterUtil | encodePassword | encodedPassword : " + encodedPassword);
		
		customer.setPassword(encodedPassword);
	}
	
	public static String getSiteURL(HttpServletRequest request) {
		
		LOGGER.info("CustomerRegisterUtil | getSiteURL is called");
		
		String siteURL = request.getRequestURL().toString();
		
		LOGGER.info("CustomerRegisterUtil | getSiteURL | siteURL :" + siteURL);
		LOGGER.info("CustomerRegisterUtil | getSiteURL | request.getServletPath() :" + request.getServletPath());
		LOGGER.info("CustomerRegisterUtil | getSiteURL | siteURL.replace(request.getServletPath(), \"\") :" 
		+ siteURL.replace(request.getServletPath(), ""));
		
		return siteURL.replace(request.getServletPath(), "");
	}

	public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
		
		LOGGER.info("CustomerRegisterUtil | prepareMailSender is called");
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost(settings.getHost());
		mailSender.setPort(settings.getPort());
		mailSender.setUsername(settings.getUsername());
		mailSender.setPassword(settings.getPassword());
		mailSender.setDefaultEncoding("utf-8");

		Properties mailProperties = new Properties();
		mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
		mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());

		mailSender.setJavaMailProperties(mailProperties);
		
		LOGGER.info("CustomerRegisterUtil | prepareMailSender | mailSender : " + mailSender.toString());

		return mailSender;
	}
	
	public static void sendVerificationEmail(HttpServletRequest request, Customer customer, SettingService settingService) 
			throws UnsupportedEncodingException, MessagingException {
		
		LOGGER.info("CustomerRegisterUtil | sendVerificationEmail is called");
		
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		
		LOGGER.info("CustomerRegisterUtil | sendVerificationEmail | emailSettings : " + emailSettings.toString());
		
		JavaMailSenderImpl mailSender = prepareMailSender(emailSettings);
		
		LOGGER.info("CustomerRegisterUtil | sendVerificationEmail | mailSender : " + mailSender.toString());

		String toAddress = customer.getEmail();
		String subject = emailSettings.getCustomerVerifySubject();
		String content = emailSettings.getCustomerVerifyContent();
		
		LOGGER.info("CustomerRegisterUtil | sendVerificationEmail | toAddress : " + toAddress);
		LOGGER.info("CustomerRegisterUtil | sendVerificationEmail | subject : " + subject);
		LOGGER.info("CustomerRegisterUtil | sendVerificationEmail | content : " + content);

		MimeMessage message = mailSender.createMimeMessage();
		
		LOGGER.info("CustomerRegisterUtil | sendVerificationEmail | MimeMessage message : " + message.toString());
		
		MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
		
		LOGGER.info("CustomerRegisterUtil | sendVerificationEmail | MimeMessageHelper helper : " + helper.toString());

		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		LOGGER.info("CustomerRegisterUtil | sendVerificationEmail | MimeMessageHelper helper : " + helper.toString());

		content = content.replace("[[name]]", customer.getFullName());
		
		LOGGER.info("CustomerRegisterUtil | sendVerificationEmail | content : " + content);

		String verifyURL = getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
		
		LOGGER.info("CustomerRegisterUtil | sendVerificationEmail | verifyURL : " + verifyURL);

		content = content.replace("[[URL]]", verifyURL);
		
		LOGGER.info("CustomerRegisterUtil | sendVerificationEmail | content : " + content);

		helper.setText(content, true);
		
		LOGGER.info("CustomerRegisterUtil | sendVerificationEmail | MimeMessageHelper helper : " + helper.toString());

		mailSender.send(message);

		
		LOGGER.info("CustomerRegisterUtil | sendVerificationEmail | to Address : " + toAddress);
		LOGGER.info("CustomerRegisterUtil | sendVerificationEmail | Verify URL : " + verifyURL);
		
	}
	
}
