package com.shopme.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "states")
@NoArgsConstructor
@Getter
@Setter
public class State  extends IdBasedEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 45)
	private String name;

	//@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;

	public State(String name, Country country) {
		this.name = name;
		this.country = country;
	}

	@Override
	public String toString() {
		return "State [id=" + id + ", name=" + name + "]";
	}

}