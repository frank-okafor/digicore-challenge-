package com.digicore.challenge.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.digicore.challenge.exception.ServiceException;

import io.jsonwebtoken.Claims;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	private @Autowired JWTUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		// Get authorization header and validate
		final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header == null || header.isEmpty() || !header.startsWith("Bearer ")) {
			chain.doFilter(request, response);
			return;
		}

		// Get jwt token and validate
		final String token = header.split(" ")[1].trim();

		if (!jwtUtil.validateToken(token)) {
			throw new ServiceException(401, "Authorization token expired", LocalDateTime.now().toString());
		}

		Claims claims = jwtUtil.getAllClaimsFromToken(token);

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				claims.getSubject(), null, getGrantedAuthorities());

		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}

	private List<GrantedAuthority> getGrantedAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.clear();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return authorities;
	}

}