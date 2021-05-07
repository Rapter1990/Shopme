package com.shopme.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@NoArgsConstructor
@Getter
@Setter
public class Product implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true, length = 256, nullable = false)
	private String name;

	@Column(unique = true, length = 256, nullable = false)
	private String alias;

	@Column(length = 512, nullable = false, name = "short_description")
	private String shortDescription;

	@Column(length = 4096, nullable = false, name = "full_description")
	private String fullDescription;

	@Column(name = "created_time")
	private Date createdTime;

	@Column(name = "updated_time")
	private Date updatedTime;

	private boolean enabled;

	@Column(name = "in_stock")
	private boolean inStock;

	// Generate automatically column name as "cost"
	private float cost;

	// Generate automatically column name as "price"
	private float price;

	@Column(name = "discount_percent")
	private float discountPercent;
	
	private float length;
	private float width;
	private float height;
	private float weight;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "brand_id")	
	private Brand brand;

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + "]";
	}
	
}
