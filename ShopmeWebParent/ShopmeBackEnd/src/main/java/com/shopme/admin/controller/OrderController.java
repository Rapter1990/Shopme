package com.shopme.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.error.OrderNotFoundException;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.service.OrderService;
import com.shopme.admin.service.SettingService;
import com.shopme.admin.util.OrderUtil;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.order.Order;

@Controller
public class OrderController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

	private String defaultRedirectURL = "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";

	private OrderService orderService;
	
	private SettingService settingService;
	
	@Autowired
	public OrderController(OrderService orderService, SettingService settingService) {
		super();
		this.orderService = orderService;
		this.settingService = settingService;
	}

	@GetMapping("/orders")
	public String listFirstPage() {
		
		LOGGER.info("OrderController | listFirstPage is called");
		
		return defaultRedirectURL;
	}

	@GetMapping("/orders/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listOrders", moduleURL = "/orders") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser,
			HttpServletRequest request) {
		
		LOGGER.info("OrderController | listByPage is called");
		
		orderService.listByPage(pageNum, helper);
		OrderUtil.loadCurrencySetting(request, settingService);
		
		LOGGER.info("OrderController | listByPage | loggedUser : " + loggedUser.toString());
		
		if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Salesperson") && loggedUser.hasRole("Shipper")) {
			return "orders/orders_shipper";
		}

		return "orders/orders";
	}

	@GetMapping("/orders/detail/{id}")
	public String viewOrderDetails(@PathVariable("id") Integer id, Model model, 
			RedirectAttributes ra, HttpServletRequest request,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser
			) {
		
		LOGGER.info("OrderController | viewOrderDetails is called");
		
		try {
			Order order = orderService.get(id);
			
			LOGGER.info("OrderController | viewOrderDetails | order : " + order.toString());
			
			OrderUtil.loadCurrencySetting(request, settingService);		
			
			boolean isVisibleForAdminOrSalesperson = false;
			
			LOGGER.info("OrderController | viewOrderDetails | loggedUser : " + loggedUser.toString());

			if (loggedUser.hasRole("Admin") || loggedUser.hasRole("Salesperson")) {
				isVisibleForAdminOrSalesperson = true;
			}
			
			LOGGER.info("OrderController | viewOrderDetails | isVisibleForAdminOrSalesperson : " + isVisibleForAdminOrSalesperson);

			model.addAttribute("isVisibleForAdminOrSalesperson", isVisibleForAdminOrSalesperson);
			model.addAttribute("order", order);

			return "orders/order_details_modal";
		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("messageError", ex.getMessage());
			
			LOGGER.info("OrderController | viewOrderDetails | messageError : " + ex.getMessage());
			
			return defaultRedirectURL;
		}

	}
	
	@GetMapping("/orders/delete/{id}")
	public String deleteOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		
		LOGGER.info("OrderController | deleteOrder is called");
		
		try {
			orderService.delete(id);
			ra.addFlashAttribute("messageSuccess", "The order ID " + id + " has been deleted.");
			
			LOGGER.info("OrderController | deleteOrder | messageSuccess : " + "The order ID " + id + " has been deleted.");
			
		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("messageError", ex.getMessage());
			
			LOGGER.info("OrderController | deleteOrder | order : " + ex.getMessage());
		}

		return defaultRedirectURL;
	}
	
	@GetMapping("/orders/edit/{id}")
	public String editOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes ra,
			HttpServletRequest request) {
		
		LOGGER.info("OrderController | editOrder is called");
		
		try {
			Order order = orderService.get(id);;

			List<Country> listCountries = orderService.listAllCountries();
			
			LOGGER.info("OrderController | editOrder | order : " + order.toString());
			LOGGER.info("OrderController | editOrder | listCountries : " + listCountries);
			LOGGER.info("OrderController | editOrder | pageTitle : " + "Edit Order (ID: " + id + ")" );

			model.addAttribute("pageTitle", "Edit Order (ID: " + id + ")");
			model.addAttribute("order", order);
			model.addAttribute("listCountries", listCountries);

			return "orders/order_form";

		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			
			LOGGER.info("OrderController | editOrder | message : " + ex.getMessage());
			
			return defaultRedirectURL;
		}

	}
	
	@PostMapping("/order/save")
	public String saveOrder(Order order, HttpServletRequest request, RedirectAttributes ra) {
		
		LOGGER.info("OrderController | saveOrder is called");
		
		String countryName = request.getParameter("countryName");
		
		LOGGER.info("OrderController | saveOrder | countryName : " + countryName);
		
		order.setCountry(countryName);

		OrderUtil.updateProductDetails(order, request);
		OrderUtil.updateOrderTracks(order, request);

		orderService.save(order);		

		ra.addFlashAttribute("messageSuccess", "The order ID " + order.getId() + " has been updated successfully");
		
		LOGGER.info("OrderController | saveOrder | message : " + "The order ID " + order.getId() + " has been updated successfully");

		return defaultRedirectURL;
	}

		
}
