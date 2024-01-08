package com.springrestapi.blogrestapi.service;

import java.util.List;

import com.springrestapi.blogrestapi.payload.CommentDTO;

public interface CommentService {
	CommentDTO createCommentByPostId(long postId, CommentDTO commentDTO);

	List<CommentDTO> getCommentsByPostId(long postId);

	CommentDTO findCommentById(long postId, long CommentId);

	CommentDTO updateCommentById(long postId, long CommentId, CommentDTO commentDTO);

	void deleteCommentById(long postId, long CommentId);

}
