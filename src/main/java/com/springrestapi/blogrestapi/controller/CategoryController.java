package com.springrestapi.blogrestapi.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springrestapi.blogrestapi.payload.CategoryDTO;
import com.springrestapi.blogrestapi.service.CategoryService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "CRUD REST APIs for Category Resources")
public class CategoryController {
	
	private CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@SecurityRequirement(
			name="Bearer Authentication")
	public ResponseEntity<CategoryDTO> addCategory (@RequestBody CategoryDTO CategoryDTO){
		CategoryDTO savedCategoryDTO = categoryService.addCategory(CategoryDTO);
		return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CategoryDTO> getCategoryByID(@PathVariable Long id){
		CategoryDTO categoryDTO = categoryService.getCategoryById(id);
		return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
	}
	
	
	//important return type is =  <List<CategoryDTO>>!!!!!
	@GetMapping
	public ResponseEntity <List<CategoryDTO>> getAllCategories(){
		List<CategoryDTO> categoriesDTO = categoryService.getAllCategories();
		return new ResponseEntity<>(categoriesDTO, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@SecurityRequirement(
			name="Bearer Authentication")
	public ResponseEntity<CategoryDTO> updateCategoryById(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO){
		CategoryDTO updatedCategoryDTO = categoryService.updateCategoryById(categoryDTO, id);
		return new ResponseEntity<>(updatedCategoryDTO, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@SecurityRequirement(
			name="Bearer Authentication")
	public ResponseEntity<Object> deleteCategoryById(@PathVariable Long id){
		categoryService.deleteCategoryById(id);
		return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
	}
}
