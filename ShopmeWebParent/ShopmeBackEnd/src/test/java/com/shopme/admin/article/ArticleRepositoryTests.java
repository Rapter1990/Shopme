package com.shopme.admin.article;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.repository.ArticleRepository;
import com.shopme.common.entity.article.Article;
import com.shopme.common.entity.article.ArticleType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ArticleRepositoryTests {

	@Autowired 
	private ArticleRepository repo;

	@Test
	public void testListMenuBoundArticles() {
		List<Article> listArticles = repo.findByTypeOrderByTitle(ArticleType.MENU_BOUND);
		assertThat(listArticles).isNotEmpty();

		listArticles.forEach(System.out::println);
	}
}
