package com.shopme.common.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.shopme.common.constants.Constants;

import javax.persistence.JoinColumn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "brands")
@NoArgsConstructor
@Getter
@Setter
public class Brand extends IdBasedEntity implements Serializable{

	@Column(nullable = false, length = 45, unique = true)
	private String name;

	@Column(nullable = false, length = 128)
	private String logo;
	
	@ManyToMany
	@JoinTable(
			name = "brands_categories",
			joinColumns = @JoinColumn(name = "brand_id"),
			inverseJoinColumns = @JoinColumn(name = "category_id")
			)
	private Set<Category> categories = new HashSet<>();
	
	public Brand(String name) {
		this.name = name;
		this.logo = "brand-logo.png";
	}
	
	public Brand(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Brand(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + ", categories=" + categories + "]";
	}
	
	@Transient
	public String getLogoPath() {
		if (this.id == null) return "/images/image-thumbnail.png";

		return Constants.S3_BASE_URI + "/brand-logos/" + this.id + "/" + this.logo;		
	}
}
