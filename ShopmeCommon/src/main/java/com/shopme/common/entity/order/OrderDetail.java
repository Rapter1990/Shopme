package com.shopme.common.entity.order;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.IdBasedEntity;
import com.shopme.common.entity.product.Product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_details")
@NoArgsConstructor
@Getter
@Setter
public class OrderDetail extends IdBasedEntity implements Serializable{

	private int quantity;
	private float productCost;
	private float shippingCost;
	private float unitPrice;
	private float subtotal;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	public OrderDetail(String categoryName, int quantity, float productCost, float shippingCost, float subtotal) {
		this.product = new Product();
		this.product.setCategory(new Category(categoryName));
		this.quantity = quantity;
		this.productCost = productCost * quantity;
		this.shippingCost = shippingCost;
		this.subtotal = subtotal;
	}

	public OrderDetail(int quantity, String productName, float productCost, float shippingCost, float subtotal) {
		this.product = new Product(productName);
		this.quantity = quantity;
		this.productCost = productCost * quantity;
		this.shippingCost = shippingCost;
		this.subtotal = subtotal;
	}
}
