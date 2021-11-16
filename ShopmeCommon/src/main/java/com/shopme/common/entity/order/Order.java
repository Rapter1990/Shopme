package com.shopme.common.entity.order;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shopme.common.entity.AbstractAddress;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
@Setter
public class Order extends AbstractAddress implements Serializable{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Order.class);
	
	@Column(nullable = false, length = 45)
	private String country;

	private Date orderTime;

	private float shippingCost;
	private float productCost;
	private float subtotal;
	private float tax;
	private float total;

	private int deliverDays;
	private Date deliverDate;

	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderDetail> orderDetails = new HashSet<>();
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("updatedTime ASC")
	private List<OrderTrack> orderTracks = new ArrayList<>();
	
	public Order(Integer id, Date orderTime, float productCost, float subtotal, float total) {
		this.id = id;
		this.orderTime = orderTime;
		this.productCost = productCost;
		this.subtotal = subtotal;
		this.total = total;
	}
	
	public void copyAddressFromCustomer() {
		setFirstName(customer.getFirstName());
		setLastName(customer.getLastName());
		setPhoneNumber(customer.getPhoneNumber());
		setAddressLine1(customer.getAddressLine1());
		setAddressLine2(customer.getAddressLine2());
		setCity(customer.getCity());
		setCountry(customer.getCountry().getName());
		setPostalCode(customer.getPostalCode());
		setState(customer.getState());		
	}
	
	public void copyShippingAddress(Address address) {
		setFirstName(address.getFirstName());
		setLastName(address.getLastName());
		setPhoneNumber(address.getPhoneNumber());
		setAddressLine1(address.getAddressLine1());
		setAddressLine2(address.getAddressLine2());
		setCity(address.getCity());
		setCountry(address.getCountry().getName());
		setPostalCode(address.getPostalCode());
		setState(address.getState());			
	}
	
	@Transient
	public String getDestination() {
		String destination =  city + ", ";
		if (state != null && !state.isEmpty()) destination += state + ", ";
		destination += country;

		return destination;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", subtotal=" + subtotal + ", paymentMethod=" + paymentMethod + ", status=" + status
				+ ", customer=" + customer.getFullName() + "]";
	}
	
	@Transient
	public String getShippingAddress() {
		String address = firstName;

		if (lastName != null && !lastName.isEmpty()) address += " " + lastName;

		if (!addressLine1.isEmpty()) address += ", " + addressLine1;

		if (addressLine2 != null && !addressLine2.isEmpty()) address += ", " + addressLine2;

		if (!city.isEmpty()) address += ", " + city;

		if (state != null && !state.isEmpty()) address += ", " + state;

		address += ", " + country;

		if (!postalCode.isEmpty()) address += ". Postal Code: " + postalCode;
		if (!phoneNumber.isEmpty()) address += ". Phone Number: " + phoneNumber;

		return address;
	}
	
	@Transient
	public String getDeliverDateOnForm() {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		//dateFormatter.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Istanbul"));
		return dateFormatter.format(this.deliverDate);
	}
	
	public void setDeliverDateOnForm(String dateString) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		//dateFormatter.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Istanbul"));
		try {
			this.deliverDate = dateFormatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		} 		
	}
	
	@Transient
	public String getRecipientName() {
		String name = firstName;
		if (lastName != null && !lastName.isEmpty()) name += " " + lastName;
		return name;
	}

	@Transient
	public String getRecipientAddress() {
		String address = addressLine1;

		if (addressLine2 != null && !addressLine2.isEmpty()) address += ", " + addressLine2;

		if (!city.isEmpty()) address += ", " + city;

		if (state != null && !state.isEmpty()) address += ", " + state;

		address += ", " + country;

		if (!postalCode.isEmpty()) address += ". " + postalCode;

		return address;
	}	

	@Transient
	public boolean isCOD() {
		return paymentMethod.equals(PaymentMethod.COD);
	}

	@Transient
	public boolean isPicked() {
		return hasStatus(OrderStatus.PICKED);
	}

	@Transient
	public boolean isShipping() {
		return hasStatus(OrderStatus.SHIPPING);
	}

	@Transient
	public boolean isDelivered() {
		return hasStatus(OrderStatus.DELIVERED);
	}

	@Transient
	public boolean isReturned() {
		return hasStatus(OrderStatus.RETURNED);
	}
	
	@Transient
	public boolean isReturnRequested() {
		return hasStatus(OrderStatus.RETURN_REQUESTED);
	}
	
	@Transient
	public boolean isProcessing() {
		return hasStatus(OrderStatus.PROCESSING);
	}

	public boolean hasStatus(OrderStatus status) {
		
		LOGGER.info("Order | hasStatus is called");
		
		LOGGER.info("Order | hasStatus | status : " + status.toString());
		
		for (OrderTrack aTrack : orderTracks) {
			if (aTrack.getStatus().equals(status)) {
				
				LOGGER.info("Order | hasStatus | return True ");
				
				return true;
			}
		}
		
		LOGGER.info("Order | hasStatus | return False ");
		
		return false;
	}
	
	@Transient
	public String getProductNames() {
		String productNames = "";

		productNames = "<ul>";

		for (OrderDetail detail : orderDetails) {
			productNames += "<li>" + detail.getProduct().getShortName() + "</li>";			
		}

		productNames += "</ul>";

		return productNames;
	}	
}
