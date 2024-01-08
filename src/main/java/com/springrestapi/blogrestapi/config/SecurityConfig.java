package com.springrestapi.blogrestapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springrestapi.blogrestapi.security.JWTAuthenticationEntryPoint;
import com.springrestapi.blogrestapi.security.JwtAuthenticationFilter;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@EnableMethodSecurity
@SecurityScheme(name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class SecurityConfig {

	//private UserDetailsService userDetailService;

	private JWTAuthenticationEntryPoint jWTAuthenticationEntryPoint;

	private JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfig(UserDetailsService userDetailService, JWTAuthenticationEntryPoint jWTAuthenticationEntryPoint,
			JwtAuthenticationFilter jwtAuthenticationFilter) {
		// this.userDetailService = userDetailService;
		this.jWTAuthenticationEntryPoint = jWTAuthenticationEntryPoint;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;

	}

	@Bean
	static PasswordEncoder appPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		/* This is to configure the basic authentication */
		http.csrf((csrf) -> csrf.disable()).authorizeHttpRequests((authorize) ->
		// authorize.anyRequest().authenticated()) //this is to grand all request
		authorize.requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll().requestMatchers("/api/v1/auth/**").permitAll()
				.requestMatchers("/swagger-ui/**").permitAll().requestMatchers("/v3/api-docs/**").permitAll()
				.anyRequest().authenticated())
				.exceptionHandling((exception) -> exception.authenticationEntryPoint(jWTAuthenticationEntryPoint))
				.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// jwtAuthenticationFilter will be executed before spring security filters!! ->
		// validate tokens
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	/*
	 * AuthenticationManager will use the userDetailService to get the userdetials
	 * from the DB
	 * 
	 * AuthenticationManager also use the passwordEncoder to encode and decode the
	 * password so the in memory object form line 87- 103 not required
	 * 
	 */
	@Bean
	AuthenticationManager passwordEncoder(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//		return configuration.getAuthenticationManager();
//	}

//	@Bean
//	public UserDetailsService userDetailService() {
//		UserDetails testUser = User.builder()
//				.username("test")
//				.password(passwordEncoder().encode("user")) 
//				.roles("USER")
//				.build();
//		
//		UserDetails superUser = User.builder()
//				.username("su")
//				.password(passwordEncoder().encode("user"))
//				.roles("ADMIN")
//				.build();
//		
//		return new InMemoryUserDetailsManager(testUser, superUser);
//				
//	}

}
