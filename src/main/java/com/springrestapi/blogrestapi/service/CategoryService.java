package com.springrestapi.blogrestapi.service;

import java.util.List;

import com.springrestapi.blogrestapi.payload.CategoryDTO;

public interface CategoryService {

	CategoryDTO addCategory(CategoryDTO categoryDTO);

	CategoryDTO getCategoryById(Long categoryId);

	List<CategoryDTO> getAllCategories();

	CategoryDTO updateCategoryById(CategoryDTO categoryDTO, Long categoryId);

	void deleteCategoryById(Long categoryId);
}
