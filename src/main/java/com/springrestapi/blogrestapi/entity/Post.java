package com.springrestapi.blogrestapi.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;

@Builder
@Entity
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String title;
	private String description;
	private String contents;

	/*
	 * Post and Category is bidirectional one to many & many to one relationship
	 * 
	 * thus the annotation is at both entities
	 * 
	 */

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private Set<Comment> comments = new HashSet<>();

	public Post() {
	}

	public Post(long id, String title, String description, String contents) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.contents = contents;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", title=" + title + ", description=" + description + ", contents=" + contents
				+ ", category=" + category + ", comments=" + comments + "]";
	}

	public Post(long id, String title, String description, String contents, Category category, Set<Comment> comments) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.contents = contents;
		this.category = category;
		this.comments = comments;
	}

}
