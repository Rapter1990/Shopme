package com.shopme.common.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class AbstractAddress extends IdBasedEntity{

	@Column(name = "first_name", nullable = false, length = 45)
	protected String firstName;

	@Column(name = "last_name", nullable = false, length = 45)
	protected String lastName;

	@Column(name = "phone_number", nullable = false, length = 15)
	protected String phoneNumber;

	@Column(name = "address_line_1", nullable = false, length = 64)
	protected String addressLine1;

	@Column(name = "address_line_2", length = 64)
	protected String addressLine2;

	@Column(nullable = false, length = 45)
	protected String city;

	@Column(nullable = false, length = 45)
	protected String state;

	@Column(name = "postal_code", nullable = false, length = 10)
	protected String postalCode;
}
