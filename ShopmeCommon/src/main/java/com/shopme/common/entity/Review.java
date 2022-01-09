package com.shopme.common.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	
	private int votes;
	
	@Transient
	private boolean upvotedByCurrentCustomer;

	@Transient
	private boolean downvotedByCurrentCustomer;
	
	public Review(Integer id) { 
		
		this.id = id; 
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Review other = (Review) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Review [headline=" + headline + ", rating=" + rating + ", reviewTime=" + reviewTime + ", product="
				+ product.getShortName() + ", customer=" + customer.getFullName() + "]";
	}
}
