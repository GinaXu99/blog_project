package com.springrestapi.blogrestapi.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.springrestapi.blogrestapi.entity.Role; 
import com.springrestapi.blogrestapi.entity.User;
import com.springrestapi.blogrestapi.exception.BlogAPIException;
import com.springrestapi.blogrestapi.payload.LoginDTO;
import com.springrestapi.blogrestapi.payload.RegisterDTO;
import com.springrestapi.blogrestapi.repository.RoleRepository;
import com.springrestapi.blogrestapi.repository.UserRepository;
import com.springrestapi.blogrestapi.security.JwtTokenProvider;
import com.springrestapi.blogrestapi.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	// AuthenticationManager provide authenticate functionality
	private AuthenticationManager authenticationManager;
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private JwtTokenProvider jwtTokenProvider;

	public AuthServiceImpl(AuthenticationManager authenticationManager, 
			UserRepository userRepository,
			RoleRepository roleRepository, 
			PasswordEncoder passwordEncoder,
			JwtTokenProvider jwtTokenProvider) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
	}


	@Override
	public String login(LoginDTO loginDTO) {

		// use authenticationmananger to authenticate
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(

				loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));

		// then store in context
		SecurityContextHolder.getContext().setAuthentication(authentication);

		
		String token = jwtTokenProvider.generateToken(authentication); 
		
		
		// then build APi will internally call login method
		return token;
	}


	@Override
	public String register(RegisterDTO registerDTO) {
		//step 1. check validation ->  whether username exists in DB already
		if(userRepository.existsByUsername(registerDTO.getUsername())){
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "username already exist");
		}
		
		//step 2. check validation -> check if email already exists in DB
		if(userRepository.existsByEmail(registerDTO.getEmail())){
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "email already exist");
		}
		
		//step 3. set all the values for the new user
		User user = new User();
		user.setEmail(registerDTO.getEmail());
		user.setName(registerDTO.getName());
		//setp4: encode the password before saving to DB
		user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
		user.setUsername(registerDTO.getUsername());
		
		//setp5: set the ROLE for the user
		Set<Role> roles = new HashSet<>();
		//default new user to = ROLE_USER when firstly added 
		Role userRole = roleRepository.findByName("ROLE_USER").get();
		roles.add(userRole); 
		user.setRoles(roles);
		
		//step6: save the user 
		userRepository.save(user);
		
		//step7: return the message
		return "new user registered successfully";
	}

}
