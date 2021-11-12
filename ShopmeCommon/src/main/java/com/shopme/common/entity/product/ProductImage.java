package com.shopme.common.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.shopme.common.constants.Constants;
import com.shopme.common.entity.IdBasedEntity;

@Entity
@Table(name = "product_images")
public class ProductImage extends IdBasedEntity{

	@Column(nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public ProductImage() {
		
	}
	
	public ProductImage(Integer id, String name, Product product) {
		this.id = id;
		this.name = name;
		this.product = product;
	}

	public ProductImage(String name, Product product) {
		this.name = name;
		this.product = product;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	@Transient
	public String getImagePath() {
		return Constants.S3_BASE_URI + "/product-images/" + product.getId() + "/extras/" + this.name;
	}

}
