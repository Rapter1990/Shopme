package com.shopme.util;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.shopme.common.entity.EmailSettingBag;
import com.shopme.service.SettingService;

public class CustomerForgetPasswordUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerForgetPasswordUtil.class);
	
	public static void sendEmail(String link, String email, SettingService settingService) 
			throws UnsupportedEncodingException, MessagingException {
		
		LOGGER.info("ForgotPasswordController | sendEmail is called");
		
		
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		
		LOGGER.info("CustomerForgetPasswordUtil | sendEmail | emailSettings : " + emailSettings.toString());
		
		JavaMailSenderImpl mailSender = CustomerRegisterUtil.prepareMailSender(emailSettings);
		
		LOGGER.info("CustomerForgetPasswordUtil | sendEmail | mailSender : " + mailSender.toString());

		String toAddress = email;
		String subject = "Here's the link to reset your password";

		String content = "<p>Hello,</p>"
				+ "<p>You have requested to reset your password.</p>"
				+ "Click the link below to change your password:</p>"
				+ "<p><a href=\"" + link + "\">Change my password</a></p>"
				+ "<br>"
				+ "<p>Ignore this email if you do remember your password, "
				+ "or you have not made the request.</p>";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
		
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);		

		helper.setText(content, true);
		
		LOGGER.info("CustomerForgetPasswordUtil | sendEmail | helper : " + helper.toString());
		LOGGER.info("CustomerForgetPasswordUtil | sendEmail | message : " + message.toString());
		
		mailSender.send(message);
	}
}
