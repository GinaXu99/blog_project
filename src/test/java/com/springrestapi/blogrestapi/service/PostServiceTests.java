package com.springrestapi.blogrestapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.springrestapi.blogrestapi.entity.Category;
import com.springrestapi.blogrestapi.entity.Post;
import com.springrestapi.blogrestapi.payload.PostDTO;
import com.springrestapi.blogrestapi.repository.CategoryRepository;
import com.springrestapi.blogrestapi.repository.PostRepository;
import com.springrestapi.blogrestapi.service.impl.CategoryServiceImpl;
import com.springrestapi.blogrestapi.service.impl.PostServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {

	@Mock
	private PostRepository postRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private PostServiceImpl postServiceImpl;

	@InjectMocks
	private CategoryServiceImpl categoryServiceImpl;

	private Post post;
	private PostDTO postDTO;
	private Category category;

	@BeforeEach
	public void setup() {
		post = Post
				.builder()
				.id(1L)
				.title("post1")
				.description("post1")
				.contents("this is post1")
				.category(category)
				.build();
		postDTO = PostDTO.builder()
				.id(1L)
				.title("post1")
				.description("post1")
				.contents("this is post1")
				.build();
		category = Category
				.builder()
				.id(1L)
				.name("category1")
				.description("category1 description")
				.build();
		
		postDTO = PostDTO.builder()
				.id(1L)
				.title("post1")
				.description("post1")
				.contents("this is post1")
				.build();
		
}

	@Test
	public void givenwhenSavePost_thenReturnSavedPost() {	
		when(categoryRepository.findById(postDTO.getCategoryId())).thenReturn(Optional.of(category));
		when(modelMapper.map(postDTO, Post.class)).thenReturn(post);
		post.setCategory(category);	
		when(postRepository.save(post)).thenReturn(post);
		when(modelMapper.map(post, PostDTO.class)).thenReturn(postDTO);
		PostDTO saveddto =  postServiceImpl.createPost(postDTO);
		assertThat(saveddto).isNotNull();
		assertThat(saveddto.getId()).isEqualTo(1L);
	}

	@Test
	public void givenPostId_whenFindPostById_thenReturnPost() {
		when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
		when(modelMapper.map(post, PostDTO.class)).thenReturn(postDTO);
		postDTO = postServiceImpl.findPostById(post.getId());
		assertThat(postDTO).isNotNull();
		assertThat(postDTO.getId()).isEqualTo(1L);
	}
	
	@Test
	public void givenPostId_whenDeleteById_thenReturnSuccess() {
		when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
		Long postId = post.getId();
		willDoNothing().given(postRepository).deleteById(postId);
		postServiceImpl.deletePostById(postId);
		verify(postRepository,times(1)).deleteById(postId);
		
	}
	
	@Test 
	public void givenUpdatePost_whenupdatebyId_thenReturnUpdatedPost() {
		
		when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
		when(categoryRepository.findById(postDTO.getCategoryId())).thenReturn(Optional.of(category));
		
		post.setCategory(category);
		post.setDescription(postDTO.getDescription());
		post.setTitle(postDTO.getTitle());
		post.setContents(postDTO.getContents());
		
		when(postRepository.save(post)).thenReturn(post); 
		when(modelMapper.map(post, PostDTO.class)).thenReturn(postDTO);
		
		PostDTO postDTOupdated = postServiceImpl.updatePostById(postDTO, post.getId());
		assertThat(postDTOupdated.getTitle()).isEqualTo(postDTO.getTitle());
		assertThat(postDTOupdated.getContents()).isEqualTo(postDTO.getContents());
		assertThat(postDTOupdated.getCategoryId()).isEqualTo(postDTO.getCategoryId());
	
	}

}
