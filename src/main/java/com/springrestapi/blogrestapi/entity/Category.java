package com.springrestapi.blogrestapi.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;

@Entity
@Builder
public class Category {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	
	
	/*Post and Category is bi-directional one to many & many to one relationship
	 * 
	 * thus the annotation is at both entities
	 * 
	 * */
	@OneToMany(mappedBy="category", cascade=CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Post> posts = new ArrayList<Post>();


	public Category(Long id, String name, String description, List<Post> posts) {

		this.id = id;
		this.name = name;
		this.description = description;
		this.posts = posts;
	}


	public Category() {
		
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public List<Post> getPosts() {
		return posts;
	}


	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
}
