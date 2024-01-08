package com.springrestapi.blogrestapi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.springrestapi.blogrestapi.entity.Category;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class CategoryRepositoryTests {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Test
	@DisplayName("Test find category by id")
	public void testFindCategoryById() {
		Category C1 = Category.builder()
				.name("C1")
				.description("description")
				.build();
		
		categoryRepository.save(C1);
		Category category = categoryRepository.findById(C1.getId()).get();
		assertThat(category).isNotNull();
		assertThat(category.getId()).isGreaterThan(0);
	}
}
