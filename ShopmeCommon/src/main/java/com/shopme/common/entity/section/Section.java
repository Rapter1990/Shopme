package com.shopme.common.entity.section;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.shopme.common.entity.IdBasedEntity;
import com.shopme.common.entity.article.Article;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sections")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Section extends IdBasedEntity{
	
	@Column(length = 256, nullable = false, unique = true)
	private String heading;

	@Column(length = 2048, nullable = false)
	private String description;

	private boolean enabled;

	@Column(name = "section_order")
	private int sectionOrder;

	@Enumerated(EnumType.STRING)
	private SectionType type;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "section_id")
	@OrderBy("productOrder ASC")
	private List<ProductSection> productSections = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "section_id")
	@OrderBy("categoryOrder ASC")
	private List<CategorySection> categorySections = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "section_id")
	@OrderBy("brandOrder ASC")
	private List<BrandSection> brandSections = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "section_id")
	@OrderBy("articleOrder ASC")
	private List<ArticleSection> articleSections = new ArrayList<>();
	
	public Section(boolean enabled, SectionType type) {
		this.enabled = enabled;
		this.type = type;
	}
	
	public Section(Integer id, String heading, SectionType type, int order, boolean enabled) {
		this.id = id;
		this.heading = heading;
		this.type = type;
		this.sectionOrder = order;
		this.enabled = enabled;
	}

	public Section(Integer id) {
		this.id = id;
	}
	
	public void addProductSection(ProductSection productSection) {
		this.productSections.add(productSection);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Section other = (Section) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Section [id=" + id + ", heading=" + heading + ", enabled=" + enabled + ", sectionOrder=" + sectionOrder + ", type="
				+ type + "]";
	}

	public void addArticleSection(ArticleSection articleSection) {
		// TODO Auto-generated method stub
		this.articleSections.add(articleSection);
	}

	public void addBrandSection(BrandSection brandSection) {
		// TODO Auto-generated method stub
		this.brandSections.add(brandSection);
	}

	public void addCategorySection(CategorySection categorySection) {
		// TODO Auto-generated method stub
		this.categorySections.add(categorySection);
	}
}
