package com.springrestapi.blogrestapi.controller;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springrestapi.blogrestapi.payload.PaginationPostResponse;
import com.springrestapi.blogrestapi.payload.PostDTO;
import com.springrestapi.blogrestapi.service.PostService;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTests {

	@MockBean
	private PostService postService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;


	@Test
	@WithMockUser
	public void getAllPost_withAuth() throws JsonProcessingException, Exception {
		PostDTO postDTO1 = PostDTO.builder()
				.id(1L)
				.title("post1")
				.description("post1 description")
				.contents("this is post1")
				.build();
		PostDTO postDTO2 = PostDTO.builder()
				.id(1L)
				.title("post1")
				.description("post1 description")
				.contents("this is post1")
				.build();
		List<PostDTO> postdtolists = new ArrayList<>();
		postdtolists.add(postDTO2);
		postdtolists.add(postDTO1);
		PaginationPostResponse postPaginationlist = PaginationPostResponse.builder()
				.content(postdtolists)
				.pageNo(1)
				.pageSize(1)
				.totalElements(2)
				.totalPages(2)
				.build();
		
		given(postService.getallPosts(Mockito.anyInt(),Mockito.anyInt(), Mockito.anyString(),Mockito.anyString()))
				.willReturn(postPaginationlist);
		ResultActions response = mockMvc.perform(get("/api/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(postPaginationlist)));
		response.andDo(print()).andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void getPostById_withAuth() throws JsonProcessingException, Exception {
		PostDTO postDTO = PostDTO.builder()
				.id(2)
				.title("post1")
				.description("post1")
				.contents("this is post1")
				.build();
		given(postService.findPostById(Mockito.anyLong())).willReturn(postDTO);

		ResultActions response = mockMvc.perform(get("/api/posts/{id}",postDTO.getId() )
				.contentType(MediaType.APPLICATION_JSON)
				.contentType(objectMapper.writeValueAsString(postDTO)));
		response.andDo(print())
				.andExpectAll(status().isOk());
	}
	

	@Test
	@WithMockUser(authorities = {"ROLE_ADMIN"})
	public void createdPost_withAuth() throws JsonProcessingException, Exception {

		PostDTO postDTO = PostDTO.builder()
				.id(1L)
				.title("post1")
				.description("post1 description")
				.contents("this is post1")
				.build();

		given(postService.createPost(Mockito.any())).willReturn(postDTO);
	
		ResultActions response = mockMvc.perform(post("/api/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(postDTO)));
		response.andDo(print()).andExpect(status().isCreated());
		
	}

	@Test
	@WithMockUser(authorities = {"ROLE_ADMIN"})
	public void deletePostById_withAuth() throws Exception {
		Long postId = 1L;
		willDoNothing().given(postService).deletePostById(postId);
		ResultActions response = mockMvc.perform(delete("/api/posts/{id}", postId));
		response.andExpect(status().isOk()).andDo(print());
	}

	@Test
	@WithMockUser(authorities = {"ROLE_ADMIN"})
	public void updatedPostById_withAuth() throws JsonProcessingException, Exception {
	
		PostDTO updatedpostDTO = PostDTO.builder().id(1L).title("post1 updated")
				.description("post1 description updated").contents("this is post1 updated").build();

		given(postService.updatePostById(Mockito.any(), Mockito.anyLong())).willReturn(updatedpostDTO);
		ResultActions response = mockMvc.perform(put("/api/posts/{id}", updatedpostDTO.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedpostDTO)));
		response.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void updatedPostById_withoutAuth() throws JsonProcessingException, Exception {
	
		PostDTO updatedpostDTO = PostDTO.builder().id(1L).title("post1 updated")
				.description("post1 description updated").contents("this is post1 updated").build();

		given(postService.updatePostById(Mockito.any(), Mockito.anyLong())).willReturn(updatedpostDTO);
		ResultActions response = mockMvc.perform(put("/api/posts/{id}", updatedpostDTO.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedpostDTO)));
		response.andExpect(status().isUnauthorized());
	}

}
