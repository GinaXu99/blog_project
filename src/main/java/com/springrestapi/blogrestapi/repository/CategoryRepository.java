package com.springrestapi.blogrestapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springrestapi.blogrestapi.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	Optional<Category> findById(Long categoryId);
}
