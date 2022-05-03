package com.shopme.common.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "countries")
@NoArgsConstructor
@Getter
@Setter
public class Country extends IdBasedEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 45)
	private String name;

	@Column(nullable = false, length = 5)
	private String code;

	//@JsonBackReference
	@OneToMany(mappedBy = "country")
	private Set<State> states;

	public Country(String name, String code) {
		this.name = name;
		this.code = code;
	}
	
	public Country(Integer id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}
	
	public Country(String name) {
		this.name = name;
	}
	
	public Country(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + ", code=" + code + "]";
	}

}
