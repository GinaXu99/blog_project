package com.springrestapi.blogrestapi.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.springrestapi.blogrestapi.entity.Category;
import com.springrestapi.blogrestapi.exception.ResourceNotFoundException;
import com.springrestapi.blogrestapi.payload.CategoryDTO;
import com.springrestapi.blogrestapi.repository.CategoryRepository;
import com.springrestapi.blogrestapi.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{
	
	private CategoryRepository categoryRepository;
	private ModelMapper modelMapper;
	
	
	public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
		this.categoryRepository = categoryRepository;
		this.modelMapper = modelMapper;
	}


	@Override
	public CategoryDTO addCategory(CategoryDTO categoryDTO) {
			Category category = modelMapper.map(categoryDTO, Category.class);
			Category savedCategory = categoryRepository.save(category);
			return modelMapper.map(savedCategory, CategoryDTO.class);
	}


	@Override
	public CategoryDTO getCategoryById(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId", categoryId));
		return modelMapper.map(category, CategoryDTO.class);
	}


	@Override
	public List<CategoryDTO> getAllCategories() {
		List<Category> list = categoryRepository.findAll();
		return list.stream()
				.map(category -> modelMapper.map(category, CategoryDTO.class))
				.collect(Collectors.toList());
	}


	@Override
	public CategoryDTO updateCategoryById(CategoryDTO categoryDTO, Long categoryId) {
		Category category =  categoryRepository.findById(categoryId)
			.orElseThrow(()->new ResourceNotFoundException("Category", "CategoryId", categoryId));
		
		//categoryDTO contains all the updated information 
		category.setDescription(categoryDTO.getDescription());
		category.setName(categoryDTO.getName());
		category.setId(categoryId);

		Category updatedCategory = categoryRepository.save(category);
		return modelMapper.map(updatedCategory, CategoryDTO.class);
	}


	@Override
	public void deleteCategoryById(Long categoryId) {
		categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));
		categoryRepository.deleteById(categoryId);
	}	
	
}
