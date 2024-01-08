package com.springrestapi.blogrestapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springrestapi.blogrestapi.payload.JWTAuthResponse;
import com.springrestapi.blogrestapi.payload.LoginDTO;
import com.springrestapi.blogrestapi.payload.RegisterDTO;
import com.springrestapi.blogrestapi.service.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "CRUD REST APIs for Auth Resources")
public class AuthController {
	
	private AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	} 
	
	//build login api
	@PostMapping(value = {"login", "signin"})
	public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDTO loginDTO){
		String token = authService.login(loginDTO);
		
		JWTAuthResponse jWTAuthResponse = new JWTAuthResponse();
		
		jWTAuthResponse.setAccessToken(token);
		
		return ResponseEntity.ok(jWTAuthResponse);
	}
	
	/*DB configuration*/
//	@PostMapping(value = {"login", "signin"})
//	public ResponseEntity<String> loginwithDB(@RequestBody LoginDTO loginDTO){
//		String response = authService.login(loginDTO)	; 
//		return ResponseEntity.ok(response);
//	}
	
	
	//build register/signup api
	@PostMapping(value = {"register", "signup"})
	public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO){
		String response = authService.register(registerDTO);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	
//	/*DB configuration*/
//	@PostMapping(value = {"register", "signup"})
//	public ResponseEntity<String> registerwithDB(@RequestBody RegisterDTO registerDTO){
//		String response = authService.register(registerDTO);
//		return new ResponseEntity<>(response, HttpStatus.CREATED);
//	}


}
