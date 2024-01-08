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

import com.springrestapi.blogrestapi.payload.CommentDTO;
import com.springrestapi.blogrestapi.service.CommentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/posts")
@Tag(name = "CRUD REST APIs for Comment Resources")
public class CommentController {

	private CommentService commentService;

	public CommentController(CommentService commentService) {
		super();
		this.commentService = commentService;
	}

	@PostMapping("{postId}/comments")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@SecurityRequirement(
			name="Bearer Authentication")
	public ResponseEntity<CommentDTO> createPostComment(@PathVariable long postId, @Valid @RequestBody CommentDTO commentDTO) {
		CommentDTO comment = commentService.createCommentByPostId(postId, commentDTO);
		return new ResponseEntity<>(comment, HttpStatus.CREATED);

	}

	@GetMapping("{postId}/comments")
	public ResponseEntity<List<CommentDTO>> getAllPostCommment(@PathVariable long postId) {
		List<CommentDTO> comments = commentService.getCommentsByPostId(postId);
		return new ResponseEntity<>(comments, HttpStatus.OK);
		
	}
	
	@GetMapping("{postId}/comments/{commentId}")
	public ResponseEntity<CommentDTO> getCommentById(
			@PathVariable long postId, @PathVariable long commentId){
		CommentDTO comment = commentService.findCommentById(postId, commentId); 
		return new ResponseEntity<>(comment, HttpStatus.OK);
		
	}
	
	@PutMapping("{postId}/comments/{commentId}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@SecurityRequirement(
			name="Bearer Authentication")
	public ResponseEntity<CommentDTO> updateCommentById(
			@PathVariable long postId, @PathVariable long commentId,
			@Valid @RequestBody CommentDTO commentDTO){
				
		CommentDTO comment = commentService.updateCommentById(postId, commentId, commentDTO);
		return new ResponseEntity<>(comment, HttpStatus.OK);	
		
	}
	
	@DeleteMapping("{postId}/comments/{commentId}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@SecurityRequirement(
			name="Bearer Authentication")
	public ResponseEntity<Object> deleteCommentById(@PathVariable long postId, @PathVariable long commentId){
		commentService.deleteCommentById(postId, commentId);
		return new ResponseEntity<>("successfully deleted the comment", HttpStatus.OK);	
	}
}
