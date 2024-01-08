package com.springrestapi.blogrestapi.exception;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.springrestapi.blogrestapi.payload.ErrorDetails;

/* 1. this class will handle specific and generic exceptions */

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	// handle specific exception
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex,
			WebRequest webrequest) {

		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), webrequest.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);

	}

	// handle specific exception
	@ExceptionHandler(BlogAPIException.class)
	public ResponseEntity<ErrorDetails> handleBlogAPIException(BlogAPIException ex, WebRequest webrequest) {

		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), webrequest.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

	}

	// handle global exception - 1st way: validation error exception
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		Map<String, String> errors = new HashMap<>();
		
		List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
		
		errorList.forEach((error) -> {
			String fieldName = ((FieldError)error).getField();
			String message = error.getDefaultMessage();
			errors.put(fieldName, message);
		});
		
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

	}

	// handle specific exception - 2st way: handle it as other specific exception

//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<Object> handleMethodArgumentNotValid2ndWay(MethodArgumentNotValidException ex,
//			WebRequest webrequest) {
//		
//		Map<String, String> errors =new HashMap<>();
//		ex.getBindingResult().getAllErrors().forEach((error) -> {
//			String fieldName = ((FieldError)error).getField();
//			String message = error.getDefaultMessage();
//			errors.put(fieldName, message);
//		});
//		
//		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//		
//	}
//

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException ex, WebRequest webrequest) {

		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), webrequest.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);

	}
	

	// handle global exception - all exception inherit from Exception.class!!!
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> handleGenericException(Exception ex, WebRequest webrequest) {

		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), webrequest.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
