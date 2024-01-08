package com.springrestapi.blogrestapi.service.impl;

import java.util.List;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springrestapi.blogrestapi.entity.Category;
import com.springrestapi.blogrestapi.entity.Post;
import com.springrestapi.blogrestapi.exception.ResourceNotFoundException;
import com.springrestapi.blogrestapi.payload.PostDTO;
import com.springrestapi.blogrestapi.payload.PaginationPostResponse;
import com.springrestapi.blogrestapi.repository.CategoryRepository;
import com.springrestapi.blogrestapi.repository.PostRepository;
import com.springrestapi.blogrestapi.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	private PostRepository postRepository;

	private ModelMapper modelMapper;
	
	private CategoryRepository categoryRepository; 

	public PostServiceImpl(PostRepository postRepository, 
			ModelMapper modelMapper,
			CategoryRepository categoryRepository) {
		this.postRepository = postRepository;
		this.modelMapper = modelMapper;
		this.categoryRepository = categoryRepository;
	}
	

	@Override
	public PostDTO createPost(PostDTO postDTO) {
		
		//3. add category into Post 
		Category category= categoryRepository.findById(postDTO.getCategoryId())
						.orElseThrow(()-> new ResourceNotFoundException("Category", "CategoryId", postDTO.getCategoryId()));
		
		
		// 1. convert DTO to entity
		Post post = modelMapper.map(postDTO, Post.class); 
		//Post post = mapToEntity(postDTO);
		
		//3 set category to post before saving it
		post.setCategory(category);
		
		Post newPost = postRepository.save(post);

		// 2. convert post entity to DTO
		PostDTO postResponse = modelMapper.map(newPost, PostDTO.class); 
		//PostDTO postResponse = mapToDTO(newPost);
		return postResponse;
	}
	

	
	@Override
	public PaginationPostResponse getallPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// create page request instance
		PageRequest pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<Post> posts = postRepository.findAll(pageable);

		List<Post> listOfPosts = posts.getContent();

		List<PostDTO> content = listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

		PaginationPostResponse postResponse = new PaginationPostResponse();
		postResponse.setContent(content);
		postResponse.setPageNo(posts.getNumber());
		postResponse.setPageSize(posts.getSize());
		postResponse.setTotalElements(posts.getTotalElements());
		postResponse.setTotalPages(posts.getTotalPages());
		postResponse.setLast(posts.isLast());

		return postResponse;

	}

	
	@Override
	public PostDTO findPostById(long id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "id", id));
		return mapToDTO(post);

	}

	
	@Override
	public PostDTO updatePostById(PostDTO postDto, long id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "id", id));

		
		//set categories to post 
		Category category = categoryRepository.findById(postDto.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("post", "id", id)); 
		post.setCategory(category);


		post.setContents(postDto.getContents());
		post.setDescription(postDto.getDescription());
		post.setTitle(postDto.getTitle());
		// remember to save it to DB!!
		Post updatedPost = postRepository.save(post);
		return mapToDTO(updatedPost);
	}

	
	@Override
	public void deletePostById(long id) {
		// !!! find the post first before deleting it
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "id", id));
		postRepository.deleteById(post.getId());
	}

	
	private PostDTO mapToDTO(Post post) {
		// map source, map destination
		PostDTO postResponse = modelMapper.map(post, PostDTO.class);

//		PostDTO postResponse = new PostDTO();
//		postResponse.setContents(post.getContents());
//		postResponse.setDescription(post.getDescription());
//		postResponse.setId(post.getId());
//		postResponse.setTitle(post.getTitle());	
		return postResponse;
	}

	
	private Post mapToEntity(PostDTO postDTO) {
		Post post = modelMapper.map(postDTO, Post.class);
//		Post post = new Post();
//		post.setId(postDTO.getId());
//		post.setContents(postDTO.getContents());
//		post.setDescription(postDTO.getDescription());
//		post.setTitle(postDTO.getTitle());
		return post;

	}


	@Override
	public List<PostDTO> getPostByCategory(Long categoryId) {
		categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
		
		List<Post> posts = postRepository.findByCategoryId(categoryId); 
		
		return  posts.stream()
				.map( post -> mapToDTO(post)).collect(Collectors.toList());
	
	}
}
