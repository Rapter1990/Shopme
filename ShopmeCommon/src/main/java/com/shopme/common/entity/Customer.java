package com.shopme.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "customers")
@NoArgsConstructor
@Getter
@Setter
public class Customer extends AbstractAddressWithCountry implements Serializable{

	@Column(nullable = false, unique = true, length = 45)
	private String email;

	@Column(nullable = false, length = 64)
	private String password;

	@Column(name = "verification_code", length = 64)
	private String verificationCode;	

	private boolean enabled;

	@Column(name = "created_time")
	private Date createdTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "authentication_type", length = 10)
	private AuthenticationType authenticationType;
	
	@Column(name = "reset_password_token", length = 30)
	private String resetPasswordToken;
	
	public Customer(Integer id) {
		this.id = id;
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	@Transient
	public String getAddress() {
		String address = firstName;

		if (lastName != null && !lastName.isEmpty()) address += " " + lastName;

		if (!addressLine1.isEmpty()) address += ", " + addressLine1;

		if (addressLine2 != null && !addressLine2.isEmpty()) address += ", " + addressLine2;

		if (!city.isEmpty()) address += ", " + city;

		if (state != null && !state.isEmpty()) address += ", " + state;

		address += ", " + country.getName();

		if (!postalCode.isEmpty()) address += ". Postal Code: " + postalCode;
		if (!phoneNumber.isEmpty()) address += ". Phone Number: " + phoneNumber;

		return address;
	}
}
