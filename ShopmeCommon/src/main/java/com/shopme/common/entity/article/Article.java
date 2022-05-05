package com.shopme.common.entity.article;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.shopme.common.entity.IdBasedEntity;
import com.shopme.common.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "articles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Article extends IdBasedEntity implements Serializable{

	@Column(nullable = false, length = 256)
	private String title;

	@Column(nullable = false)
	@Lob
	private String content;

	@Column(nullable = false, length = 500)
	private String alias;

	@Enumerated(EnumType.ORDINAL)
	private ArticleType type;

	@Column(name = "updated_time")
	private Date updatedTime;

	private boolean published;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	
}
