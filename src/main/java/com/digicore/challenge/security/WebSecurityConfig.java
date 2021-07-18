package com.digicore.challenge.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.digicore.challenge.utils.Applicationutils;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private @Autowired JwtTokenFilter jwtTokenFilter;

	private @Autowired CustomAuthenticationEntryPoint unauthorizedHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Enable CORS and disable CSRF
		http.cors().and().csrf().disable()
				// Set session management to stateless
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				// Set unauthorized requests exception handler
				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				// Set permissions on endpoints
				.authorizeRequests()
				// Our public endpoints
				.antMatchers(Applicationutils.RESOURCE_WHITELIST).permitAll()
				// Our private endpoints
				.anyRequest().authenticated();
		// Add JWT token filter
		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
		configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration.applyPermitDefaultValues());
		return source;
	}

}
