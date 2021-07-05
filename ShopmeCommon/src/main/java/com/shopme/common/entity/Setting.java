package com.shopme.common.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "settings")
@NoArgsConstructor
@Getter
@Setter
public class Setting {
	
	@Id
	@Column(name = "`key`", nullable = false, length = 128)
	private String key;

	@Column(nullable = false, length = 1024)
	private String value;

	@Enumerated(EnumType.STRING)
	@Column(length = 45, nullable = false)
	private SettingCategory category;

	public Setting(String key, String value, SettingCategory category) {
		this.key = key;
		this.value = value;
		this.category = category;
	}

}
