package com.springrestapi.blogrestapi.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private JwtTokenProvider jwtTokenProvider;
	private UserDetailsService userDetailService;
	
	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailService) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.userDetailService = userDetailService;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, 
									HttpServletResponse response, 
									FilterChain filterChain)throws ServletException, IOException {
		
		//get JWT token from http request
		String token = getTokenFromRequest(request);
		
		//validate token
		if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
			//get username from token 
			String usernmae = jwtTokenProvider.getUsername(token);
			
			//get userfrom DB
			UserDetails userDetails = userDetailService.loadUserByUsername(usernmae);
			
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails,
					null,
					userDetails.getAuthorities()
					);
			authenticationToken.setDetails(
					new WebAuthenticationDetailsSource().buildDetails(request)
					);
			
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}
		
		
		filterChain.doFilter(request, response);
	}
	
	
	
	private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7, bearerToken.length());
        }

        return null;
		
		
	}

	
	
}
