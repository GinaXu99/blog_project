package com.springrestapi.blogrestapi.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springrestapi.blogrestapi.entity.Comment;
import com.springrestapi.blogrestapi.entity.Post;
import com.springrestapi.blogrestapi.payload.CommentDTO;
import com.springrestapi.blogrestapi.repository.CommentRepository;
import com.springrestapi.blogrestapi.repository.PostRepository;
import com.springrestapi.blogrestapi.service.CommentService;
import com.springrestapi.blogrestapi.exception.BlogAPIException;
import com.springrestapi.blogrestapi.exception.ResourceNotFoundException;


@Service
public class CommentServiceImpl implements CommentService {

	private CommentRepository commentRepository;
	private PostRepository postRepository;
	private ModelMapper modelMapper;
	
	

	public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,
			ModelMapper modelMapper) {
		super();
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
		this.modelMapper = modelMapper;
	}


	@Override
	public CommentDTO createCommentByPostId(long postId, CommentDTO commentDTO) {
		//get the post using ID
		Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("post", "id", postId));
		
		//set the comment to post 
		Comment comment = mapToEntity(commentDTO);
		comment.setPost(post);
		
		//save the comment to DB for the new comment 
		Comment newComment = commentRepository.save(comment);
		
		return mapToDTO(newComment);
	}

	
	private CommentDTO mapToDTO(Comment comment) {
		CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
		
//		CommentDTO commentDTO = new CommentDTO();
//		commentDTO.setBody(comment.getBody());
//		commentDTO.setEmail(comment.getEmail());
//		commentDTO.setId(comment.getId());
//		commentDTO.setName(comment.getName());
		return commentDTO;
	}
	
	
	private Comment mapToEntity(CommentDTO commentDTO){	
		
		
		Comment comment = modelMapper.map(commentDTO, Comment.class);
//		Comment comment = new Comment();
//		comment.setBody(commentDTO.getBody());
//		comment.setEmail(commentDTO.getEmail());
//		comment.setId(commentDTO.getId());
//		comment.setName(commentDTO.getName());
		return comment;
	
	}


	@Override
	public List<CommentDTO> getCommentsByPostId(long postId) {
		List<Comment> comments = commentRepository.findByPostId(postId);
			return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
	}


	@Override
	public CommentDTO findCommentById(long postId, long commentId) {
		//find the post 
		Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("post", "id", postId));
		//find the comment 
		Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("comment", "id", commentId));;
		//comment exists in post then throw exception else return comment
		if (comment.getPost().equals(post)) {
			return mapToDTO(comment);
		}
		throw new BlogAPIException(HttpStatus.BAD_REQUEST, "could not find comment under the post");
	}


	@Override
	public CommentDTO updateCommentById(long postId, long commentId, CommentDTO commentDTO) {
		//find the post 
		Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("post", "id", postId));
		//find the comment 
		Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("comment", "id", commentId));;
		//comment exists in post then throw exception else return comment
		if (!(comment.getPost().equals(post))) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "could not find comment under the post");
		}
		//all the fields need to be reset for update, cannot simply save it to db like line101-102
//		//commentRepository.save(comment);
//		//postRepository.save(post);
		
		//update the fields with new values
		comment.setName(commentDTO.getName());
		comment.setEmail(commentDTO.getEmail());
		comment.setBody(commentDTO.getBody());
		
		//save the updated comment to DB
		Comment updatedComment = commentRepository.save(comment);
		return mapToDTO(updatedComment);
	}


	@Override
	public void deleteCommentById(long postId, long commentId) {
		//find the post 
		Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("post", "id", postId));
		//find the comment 
		Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("comment", "id", commentId));;

		if (!(comment.getPost().equals(post))) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "could not find comment under the post");
		}
		commentRepository.deleteById(commentId);
		
	}
}
