package com.springrestapi.blogrestapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springrestapi.blogrestapi.entity.Post;


public interface PostRepository extends JpaRepository<Post, Long>{
	
	List<Post> findByCategoryId(Long CategoryId);
	
	Optional<Post> findById(Long id);
}


