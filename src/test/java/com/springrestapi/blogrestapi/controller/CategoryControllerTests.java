package com.springrestapi.blogrestapi.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springrestapi.blogrestapi.payload.CategoryDTO;
import com.springrestapi.blogrestapi.service.CategoryService;


@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTests {
	
	@Autowired
	private MockMvc mockmvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private CategoryService categoryService;
	
	
	@Test
	@WithMockUser(authorities = {"ROLE_ADMIN"})
	public void testCreateCategory_withAuth() throws Exception {
		CategoryDTO dto = CategoryDTO.builder()
				.id(1L)
				.name("C1")
				.description("description")
				.build();
		given(categoryService.addCategory(Mockito.any(CategoryDTO.class)))
				.willReturn(dto);
		
		ResultActions response = mockmvc.perform(post("/api/categories")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)));
		response.andDo(print()).andExpectAll(status().isCreated());
	}
	
	
	@Test
	@WithMockUser
	public void testGetCategoryById_withAuth() throws Exception {
		CategoryDTO dto = CategoryDTO.builder()
				.id(1L)
				.name("C1")
				.build();
		
		Long id = 1L;
		
		Mockito.when(categoryService.getCategoryById(Mockito.anyLong())).thenReturn(dto);
		ResultActions response = mockmvc.perform(get("/api/categories/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.contentType(objectMapper.writeValueAsString(dto)));
		response.andDo(print()).andExpectAll(status().isOk());
	}
	
	
	@Test
	@WithMockUser
	public void testGetAllCategories_withAuth() throws Exception {
		List<CategoryDTO> list = new ArrayList<>();
		CategoryDTO dto1 = CategoryDTO.builder()
				.id(1L)
				.name("C1")
				.build();
		
		CategoryDTO dto2 = CategoryDTO.builder()
				.id(1L)
				.name("C1")
				.build();
		list.add(dto2);
		list.add(dto1);
		
		Mockito.when(categoryService.getAllCategories()).thenReturn(list);
		ResultActions response = mockmvc.perform(get("/api/categories")
				.contentType(MediaType.APPLICATION_JSON)
				.contentType(objectMapper.writeValueAsString(list)));
		response.andDo(print()).andExpectAll(status().isOk())
		.andExpect(jsonPath("$.size()",CoreMatchers.is(list.size())));			
	}
	
	
	
	@Test
	@WithMockUser(authorities = {"ROLE_ADMIN"})
	public void testUpdateCategory_withAuth() throws Exception {
		CategoryDTO dto1 = CategoryDTO.builder()
				.id(1L)
				.name("c1 updated")
				.description("c1 description")
				.build();
		
		Mockito.when(categoryService.updateCategoryById(Mockito.any(), Mockito.anyLong())).thenReturn(dto1);
		ResultActions response = mockmvc.perform(put("/api/categories/{id}", dto1.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto1)));
		
		response.andDo(print()).andExpectAll(status().isOk())
		.andExpect(jsonPath("$.name",CoreMatchers.is(dto1.getName())))	
		.andExpect(jsonPath("$.description",CoreMatchers.is(dto1.getDescription())));
	}
	
	
	
	@Test
	@WithMockUser(authorities = {"ROLE_ADMIN"})
	public void testDeleteCategory_withAuth() throws Exception {
		CategoryDTO dto1 = CategoryDTO.builder()
				.id(1L)
				.name("c1 updated")
				.description("c1 description")
				.build();
		
		willDoNothing().given(categoryService).deleteCategoryById(dto1.getId());
		
		ResultActions response = mockmvc.perform(delete("/api/categories/{id}", dto1.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto1)));
		
		response.andDo(print()).andExpect(status().isOk());
	}

}
