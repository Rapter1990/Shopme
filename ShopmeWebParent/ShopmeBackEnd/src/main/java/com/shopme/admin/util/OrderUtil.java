package com.shopme.admin.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shopme.admin.service.SettingService;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.order.OrderTrack;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.setting.Setting;

public class OrderUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderUtil.class);

	public static void loadCurrencySetting(HttpServletRequest request, SettingService settingService) {
		
		LOGGER.info("OrderUtil | loadCurrencySetting is called");
		
		List<Setting> currencySettings = settingService.getCurrencySettings();
		
		LOGGER.info("OrderUtil | loadCurrencySetting | currencySettings : " + currencySettings.toString());

		for (Setting setting : currencySettings) {
			LOGGER.info("OrderUtil | loadCurrencySetting | setting | key : " + setting.getKey() + " , value : " + setting.getValue());
			request.setAttribute(setting.getKey(), setting.getValue());
		}	
	}
	
	public static void updateOrderTracks(Order order, HttpServletRequest request) {
		
		LOGGER.info("OrderUtil | updateOrderTracks is called");
		
		String[] trackIds = request.getParameterValues("trackId");
		String[] trackStatuses = request.getParameterValues("trackStatus");
		String[] trackDates = request.getParameterValues("trackDate");
		String[] trackNotes = request.getParameterValues("trackNotes");
		
		
		LOGGER.info("OrderUtil | updateOrderTracks | trackIds : " + trackIds.toString());
		LOGGER.info("OrderUtil | updateOrderTracks | trackStatuses : " + trackStatuses.toString());
		LOGGER.info("OrderUtil | updateOrderTracks | trackDates : " + trackDates.toString());
		LOGGER.info("OrderUtil | updateOrderTracks | trackNotes : " + trackNotes.toString());

		List<OrderTrack> orderTracks = order.getOrderTracks();
		
		LOGGER.info("OrderUtil | updateOrderTracks | orderTracks : " + orderTracks.toString());
		LOGGER.info("OrderUtil | updateOrderTracks | orderTracks.size : " + orderTracks.size());
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		
		LOGGER.info("OrderUtil | updateOrderTracks | trackIds.length : " + trackIds.length);

		for (int i = 0; i < trackIds.length; i++) {
			OrderTrack trackRecord = new OrderTrack();

			Integer trackId = Integer.parseInt(trackIds[i]);
			
			LOGGER.info("OrderUtil | updateOrderTracks | trackId : " + trackId);
			
			if (trackId > 0) {
				trackRecord.setId(trackId);
			}

			trackRecord.setOrder(order);
			trackRecord.setStatus(OrderStatus.valueOf(trackStatuses[i]));
			trackRecord.setNotes(trackNotes[i]);

			
			try {
				trackRecord.setUpdatedTime(dateFormatter.parse(trackDates[i]));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			LOGGER.info("OrderUtil | updateOrderTracks | trackRecord : " + trackRecord.toString());

			orderTracks.add(trackRecord);
		}
	}

	public static void updateProductDetails(Order order, HttpServletRequest request) {
		
		LOGGER.info("OrderUtil | updateProductDetails is called");
		
		String[] detailIds = request.getParameterValues("detailId");
		String[] productIds = request.getParameterValues("productId");
		String[] productPrices = request.getParameterValues("productPrice");
		String[] productDetailCosts = request.getParameterValues("productDetailCost");
		String[] quantities = request.getParameterValues("quantity");
		String[] productSubtotals = request.getParameterValues("productSubtotal");
		String[] productShipCosts = request.getParameterValues("productShipCost");
		
		LOGGER.info("OrderUtil | updateOrderTracks | detailIds : " + detailIds.toString());
		LOGGER.info("OrderUtil | updateOrderTracks | productIds : " + productIds.toString());
		LOGGER.info("OrderUtil | updateOrderTracks | productPrices : " + productPrices.toString());
		LOGGER.info("OrderUtil | updateOrderTracks | productDetailCosts : " + productDetailCosts.toString());
		LOGGER.info("OrderUtil | updateOrderTracks | quantities : " + quantities.toString());
		LOGGER.info("OrderUtil | updateOrderTracks | productSubtotals : " + productSubtotals.toString());
		LOGGER.info("OrderUtil | updateOrderTracks | productSubtotals : " + productShipCosts.toString());

		Set<OrderDetail> orderDetails = order.getOrderDetails();
		
		LOGGER.info("OrderUtil | updateOrderTracks | orderDetails : " + orderDetails.toString());
		LOGGER.info("OrderUtil | updateOrderTracks | orderDetails.size : " + orderDetails.size());
		
		LOGGER.info("OrderUtil | updateOrderTracks | productSubtotals : " + detailIds.length);

		for (int i = 0; i < detailIds.length; i++) {
			
			LOGGER.info("OrderUtil | updateOrderTracks | Detail ID : " + detailIds[i]);
			LOGGER.info("OrderUtil | updateOrderTracks | Prodouct ID : " + productIds[i]);
			LOGGER.info("OrderUtil | updateOrderTracks | Cost : " + productDetailCosts[i]);
			LOGGER.info("OrderUtil | updateOrderTracks | Quantity : " + quantities[i]);
			LOGGER.info("OrderUtil | updateOrderTracks | Subtotal : " + productSubtotals[i]);
			LOGGER.info("OrderUtil | updateOrderTracks | Ship cost : " + productShipCosts[i]);

			OrderDetail orderDetail = new OrderDetail();
			Integer detailId = Integer.parseInt(detailIds[i]);
			
			LOGGER.info("OrderUtil | updateOrderTracks | detailId : " + detailId);
			
			if (detailId > 0) {
				orderDetail.setId(detailId);
			}

			orderDetail.setOrder(order);
			orderDetail.setProduct(new Product(Integer.parseInt(productIds[i])));
			orderDetail.setProductCost(Float.parseFloat(productDetailCosts[i]));
			orderDetail.setSubtotal(Float.parseFloat(productSubtotals[i]));
			orderDetail.setShippingCost(Float.parseFloat(productShipCosts[i]));
			orderDetail.setQuantity(Integer.parseInt(quantities[i]));
			orderDetail.setUnitPrice(Float.parseFloat(productPrices[i]));
			
			LOGGER.info("OrderUtil | updateOrderTracks | orderDetail : " + orderDetail.toString());

			orderDetails.add(orderDetail);

		}

	}
}
