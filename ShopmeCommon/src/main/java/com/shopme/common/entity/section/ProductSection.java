package com.shopme.common.entity.section;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.shopme.common.entity.IdBasedEntity;
import com.shopme.common.entity.product.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sections_products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductSection extends IdBasedEntity {

	@Column(name = "product_order")
	private int productOrder;

	@ManyToOne
	@JoinColumn(name = "product_id")	
	private Product product;

}
