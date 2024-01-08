package com.springrestapi.blogrestapi.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public class CommentDTO {

	private long id;

	@NotEmpty(message = "body should not be empty")
	@Size(min = 10, message = "body should be at least 10 characters")
	private String body;

	@NotEmpty(message = "email should not be empty")
	@Email
	private String email;

	@NotEmpty(message = "name should not be empty")
	private String name;

	public CommentDTO() {
		super();
	}

	public CommentDTO(long id, String body, String email, String name) {
		super();
		this.id = id;
		this.body = body;
		this.email = email;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "CommentDTO [id=" + id + ", body=" + body + ", email=" + email + ", name=" + name + "]";
	}

}
