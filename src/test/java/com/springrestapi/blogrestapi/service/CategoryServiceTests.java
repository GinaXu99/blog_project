package com.springrestapi.blogrestapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.springrestapi.blogrestapi.entity.Category;
import com.springrestapi.blogrestapi.payload.CategoryDTO;
import com.springrestapi.blogrestapi.repository.CategoryRepository;
import com.springrestapi.blogrestapi.service.impl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {
	
	@Mock
	private CategoryRepository categoryRepository;
	
	@InjectMocks
	private CategoryServiceImpl categoryServiceImpl;
	
	@Mock
	private ModelMapper modelMapper;

	@Test
	@DisplayName("Test add category")
	public void testAddCategory() {
		CategoryDTO categorydto = CategoryDTO.builder()
				.name("C1")
				.id(1L)
				.description("description")
				.build();
		Category category = Category.builder()
				.name("C1")
				.id(1L)
				.description("description")
				.build();
	
		when(modelMapper.map(categorydto, Category.class)).thenReturn(category);
		when(categoryRepository.save(category)).thenReturn(category);
		when(modelMapper.map(category, CategoryDTO.class)).thenReturn(categorydto);
		CategoryDTO addedCategory  = categoryServiceImpl.addCategory(categorydto);
		assertThat(addedCategory).isNotNull();
	}
	
	@Test
	@DisplayName("Test find category by id")
	public void testFindCategoryById() {
		CategoryDTO categorydto = CategoryDTO.builder()
				.name("C1")
				.id(1L)
				.description("description")
				.build();
		Category category = Category.builder()
				.name("C1")
				.id(1L)
				.description("description")
				.build();
		
		when(categoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(category));
		when(modelMapper.map(category, CategoryDTO.class)).thenReturn(categorydto);
		CategoryDTO addedCategory  = categoryServiceImpl.getCategoryById(categorydto.getId());
		assertThat(addedCategory).isNotNull();
	}
	
	@Test
	@DisplayName("Test get all categories")
	public void testGetallCategories() {
		CategoryDTO categorydto1 = CategoryDTO.builder()
				.name("C1")
				.id(1L)
				.description("description")
				.build();
		CategoryDTO categorydto2 = CategoryDTO.builder()
				.name("C1")
				.id(1L)
				.description("description")
				.build();
		Category category1 = Category.builder()
				.name("C1")
				.id(1L)
				.description("description")
				.build();
		Category category2 = Category.builder()
				.name("C1")
				.id(1L)
				.description("description")
				.build();
		List<Category> categories = List.of(category1, category2);
		
		given(categoryRepository.findAll()).willReturn(categories);
		when(modelMapper.map(category1, CategoryDTO.class)).thenReturn(categorydto1);
		when(modelMapper.map(category2, CategoryDTO.class)).thenReturn(categorydto2);
		
		List<CategoryDTO> getalldtos  = categoryServiceImpl.getAllCategories();
		assertThat(getalldtos).isNotNull();
		assertThat(getalldtos.size()).isEqualTo(2);
	}
	
	@Test
	@DisplayName("Test udpate category by id")
	public void testUpdateCategoryById() {
		CategoryDTO categorydto = CategoryDTO.builder()
				.name("C1 DTO")
				.id(1L)
				.description("descriptionDTO")
				.build();
		Category category = Category.builder()
				.name("C1")
				.id(2L)
				.description("description")
				.build();
		
		when(categoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(category));
		category.setDescription(categorydto.getDescription());
		category.setName(categorydto.getName());
		category.setId(categorydto.getId());
		
		when(categoryRepository.save(category)).thenReturn(category);
		when(modelMapper.map(category, CategoryDTO.class)).thenReturn(categorydto);
		
		CategoryDTO updatedCategoryDTO = categoryServiceImpl.updateCategoryById(categorydto, category.getId());
		assertThat(updatedCategoryDTO).isNotNull();
		assertThat(updatedCategoryDTO.getName()).isEqualTo(categorydto.getName());
		assertThat(updatedCategoryDTO.getDescription()).isEqualTo(categorydto.getDescription());
		assertThat(updatedCategoryDTO.getId()).isEqualTo(categorydto.getId());
	}
	
	@Test
	@DisplayName("Test delete category by id")
	public void testDeleteCategoryById() {
		Category category = Category.builder()
				.name("C1")
				.id(2L)
				.description("description")
				.build();
		when(categoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(category));
		willDoNothing().given(categoryRepository).deleteById(category.getId());
		categoryServiceImpl.deleteCategoryById(category.getId());
		verify(categoryRepository, times(1)).deleteById(category.getId());
	}
	
	
	

}
