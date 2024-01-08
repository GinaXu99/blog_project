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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.springrestapi.blogrestapi.payload.PaginationPostResponse;
import com.springrestapi.blogrestapi.payload.PostDTO;
import com.springrestapi.blogrestapi.service.PostService;
import com.springrestapi.blogrestapi.utils.AppConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/posts")
@Tag(name = "CRUD REST APIs for Post Resources")
public class PostController {
	//loose coupling by inject interface not serviceimpl
	private PostService postService; 
	
	public PostController(PostService postService) {
		this.postService=postService;
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PostMapping
	@SecurityRequirement(
			name="Bearer Authentication")
	@Operation(
			summary= "Create Post REST API", 
			description ="Create Post REST API is used to save post to database")
	@ApiResponse(
			responseCode="201",
			description="Http Status 201 created")
	public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDto){
		PostDTO savedPost = postService.createPost(postDto);
		return new ResponseEntity<>(savedPost, HttpStatus.CREATED); 
	} 
	
	
	@GetMapping
	public ResponseEntity<PaginationPostResponse> getAllPost(
			@RequestParam(value=AppConstants.DEFAULT_PAGE_NUMBER, defaultValue="0", required=false) int pageNo,
			@RequestParam(value=AppConstants.DEFAULT_PAGE_SIZE, defaultValue="10", required=false) int pageSize,
			@RequestParam(value=AppConstants.DEFAULT_SORT_BY, defaultValue="id", required=false) String sortBy,
			@RequestParam(value=AppConstants.DEFAULT_SORT_DIRECTION, defaultValue="asc", required=false) String sortDir){
		PaginationPostResponse postLists = postService.getallPosts(pageNo,pageSize,sortBy,sortDir);
		return new ResponseEntity<>(postLists, HttpStatus.OK);
	}
	
	
	@GetMapping("{id}")
	public ResponseEntity<PostDTO> findPostById(@PathVariable long id){
		PostDTO post = postService.findPostById(id);
		return new ResponseEntity<>(post, HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PutMapping("{id}")
	@SecurityRequirement(
			name="Bearer Authentication")
	public ResponseEntity<PostDTO> updatePostById(@PathVariable long id, @Valid @RequestBody PostDTO postDto ){
		PostDTO post = postService.updatePostById(postDto, id);
		return new ResponseEntity<>(post, HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@DeleteMapping("{id}")
	@SecurityRequirement(
			name="Bearer Authentication")
	public ResponseEntity<String> deletePostById(@PathVariable long id){
		postService.deletePostById(id);
		//find the id and delete else throw exception
		return new ResponseEntity<>("successfully deleted post", HttpStatus.OK);
	}
	
	
	@GetMapping("/category/{categoryId}")
	public ResponseEntity<List<PostDTO>> getPostByCategory(@PathVariable Long categoryId){
		List<PostDTO> posts=postService.getPostByCategory(categoryId); 
		return new ResponseEntity<>(posts, HttpStatus.OK);
	
	}
}
