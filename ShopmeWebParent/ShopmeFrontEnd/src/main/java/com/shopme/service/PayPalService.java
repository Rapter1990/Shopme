package com.shopme.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.shopme.checkout.PayPalOrderResponse;
import com.shopme.common.exception.PayPalApiException;
import com.shopme.setting.PaymentSettingBag;

@Service
public class PayPalService {

	private static final String GET_ORDER_API = "/v2/checkout/orders/";

	@Autowired 
	private SettingService settingService;

	public boolean validateOrder(String orderId) throws PayPalApiException {
		PayPalOrderResponse orderResponse = getOrderDetails(orderId);

		return orderResponse.validate(orderId);
	}

	private PayPalOrderResponse getOrderDetails(String orderId) throws PayPalApiException {
		ResponseEntity<PayPalOrderResponse> response = makeRequest(orderId);

		HttpStatus statusCode = response.getStatusCode();

		if (!statusCode.equals(HttpStatus.OK)) {
			throwExceptionForNonOKResponse(statusCode);
		}

		return response.getBody();
	}

	private ResponseEntity<PayPalOrderResponse> makeRequest(String orderId) {
		PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
		String baseURL = paymentSettings.getURL();
		String requestURL = baseURL + GET_ORDER_API + orderId;
		String clientId = paymentSettings.getClientID();
		String clientSecret = paymentSettings.getClientSecret();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Accept-Language", "en_US");
		headers.setBasicAuth(clientId, clientSecret);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();

		return restTemplate.exchange(
				requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);
	}

	private void throwExceptionForNonOKResponse(HttpStatus statusCode) throws PayPalApiException {
		String message = null;

		switch (statusCode) {
		case NOT_FOUND: 
			message = "Order ID not found";

		case BAD_REQUEST:
			message = "Bad Request to PayPal Checkout API";

		case INTERNAL_SERVER_ERROR:
			message = "PayPal server error";

		default:
			message = "PayPal returned non-OK status code";
		}

		throw new PayPalApiException(message);
	}
}
