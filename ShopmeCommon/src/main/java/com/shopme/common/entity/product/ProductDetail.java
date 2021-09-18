package com.shopme.common.entity.product;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.shopme.common.entity.IdBasedEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_details")
@NoArgsConstructor
@Getter
@Setter
public class ProductDetail extends IdBasedEntity implements Serializable{

	@Column(nullable = false, length = 255)
	private String name;

	@Column(nullable = false, length = 255)
	private String value;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	public ProductDetail(Integer id, String name, String value, Product product) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.product = product;
	}
	
	public ProductDetail(String name, String value, Product product) {
		this.name = name;
		this.value = value;
		this.product = product;
	}

}
