package com.springrestapi.blogrestapi.security;

import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.springrestapi.blogrestapi.exception.BlogAPIException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${app.jwt-secret}")
	private String jwtSecret;

	@Value("${app.jwt-expiration-milliseconds}")
	private long jwtExpirationDate;

	// generate JWT token method
	public String generateToken(Authentication authentication) {

		// username or email are configured
		String username = authentication.getName();

		Date currentDate = new Date();

		// jwt token set to expire 7 days from current date
		Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

		String jwtTokekn = Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(expireDate)
				.signWith(key())
				.compact();

		return jwtTokekn;
	}

	// get username from JWT token
	public String getUsername(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key())
				.build()
				.parseClaimsJws(token)
				.getBody();
		String username = claims.getSubject();
		return username;
	}

	// valid JWT token
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
			return true;
		} catch (MalformedJwtException ex) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT Token");
		} catch (ExpiredJwtException ex) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Expired JWT Token");
		} catch (UnsupportedJwtException ex) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "JWT Claims string is empty");
		}
	}

	// decode the key and return the key
	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

}
