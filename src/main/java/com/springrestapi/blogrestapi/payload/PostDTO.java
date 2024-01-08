package com.springrestapi.blogrestapi.payload;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Schema(description = "postDTO model information")
@Builder
public class PostDTO {

	@Schema(description = "Id field for post")
	private long id;

	@NotEmpty(message = "title should not be emtpy")
	@Size(min = 2, message = "post title should have at least 2 characters")
	private String title;

	@NotEmpty(message = "description should not be emtpy")
	@Size(min = 10, message = "post description should have at least 10 characters")
	private String description;

	@NotEmpty(message = "contents should not be emtpy")
	private String contents;
	private Set<CommentDTO> comments;

	private Long categoryId;

	public PostDTO() {
	}

	public PostDTO(long id, String title, String description, String contents, Set<CommentDTO> comments,
			Long categoryId) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.contents = contents;
		this.comments = comments;
		this.categoryId = categoryId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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

	public Set<CommentDTO> getComments() {
		return comments;
	}

	public void setComments(Set<CommentDTO> comments) {
		this.comments = comments;
	}

}
