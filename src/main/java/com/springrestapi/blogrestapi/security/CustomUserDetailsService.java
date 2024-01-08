package com.springrestapi.blogrestapi.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springrestapi.blogrestapi.entity.User;
import com.springrestapi.blogrestapi.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;


/*
 * How to load the the user object from the DB so that spring security can perform authentication 
 * -> how: using spring security provided interface: UserDetailService 
 * & override the method loadUserByUsername
 * */


@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		//provide option for user to login using username or email
		User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
				.orElseThrow(() -> 
				new UsernameNotFoundException("useremail not found with username or email" + usernameOrEmail));

		//convert set of roles to set of granted authorities 
		Set<GrantedAuthority> authorities = user.getRoles().stream()
				.map((role) -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toSet());
		//convert grantedauthories to spring provided user security objects
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);

	}
}
