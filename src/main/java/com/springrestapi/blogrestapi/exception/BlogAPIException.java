package com.springrestapi.blogrestapi.exception;

import org.springframework.http.HttpStatus;

public class BlogAPIException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private HttpStatus status;
	private String message;

	public BlogAPIException() {
		super();
	}

	public BlogAPIException(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "BlogAPIException [status=" + status + ", message=" + message + "]";
	}

}
