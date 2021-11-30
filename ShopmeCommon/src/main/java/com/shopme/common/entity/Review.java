package com.shopme.common.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.shopme.common.entity.product.Product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
public class Review extends IdBasedEntity{

	@Column(length = 128, nullable = false)
	private String headline;

	@Column(length = 300, nullable = false)
	private String comment;

	private int rating;	

	@Column(nullable = false)
	private Date reviewTime;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@Override
	public String toString() {
		return "Review [headline=" + headline + ", rating=" + rating + ", reviewTime=" + reviewTime + ", product="
				+ product.getShortName() + ", customer=" + customer.getFullName() + "]";
	}
}
