package com.springrestapi.blogrestapi.service;

import com.springrestapi.blogrestapi.payload.PostDTO;

import java.util.List;

import com.springrestapi.blogrestapi.payload.PaginationPostResponse;

public interface PostService {

	PostDTO createPost(PostDTO postDto);

	PaginationPostResponse getallPosts(int pageNo, int pageSize, String sortBy, String sortDir);

	PostDTO findPostById(long id);

	PostDTO updatePostById(PostDTO postDto, long id);

	void deletePostById(long id);

	List<PostDTO> getPostByCategory(Long categoryId);
}
