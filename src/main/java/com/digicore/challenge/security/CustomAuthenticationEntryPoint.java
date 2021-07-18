package com.digicore.challenge.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
	    AuthenticationException authException) throws IOException, ServletException {
	log.error("Unauthorized error: {}", authException.getMessage());

	String json = String.format("{\"errorcode\": \"%s\", \"message\": \"%s\"}", 401, authException.getMessage());
	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	response.setContentType("application/json");
	response.setCharacterEncoding("UTF-8");
	response.getWriter().write(json);
    }

}
