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
	
	public Article(Integer id) {
		this.id = id;
	}
	
	public Article(Integer id, String title) {
		this.id = id;
		this.title = title;
	}
	
	public Article(Integer id, String title, ArticleType type, Date updatedTime, boolean published, User user) {
		this.id = id;
		this.title = title;
		this.type = type;
		this.updatedTime = updatedTime;
		this.published = published;
		this.user = user;
	}
	
	@Override
	public String toString() {
		return "Article [title=" + title + ", type=" + type + "]";
	}
}
