package com.digicore.challenge.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.digicore.challenge.dto.request.AuthRequest;
import com.digicore.challenge.security.JWTUtil;
import com.digicore.challenge.services.UserAccountService;
import com.digicore.challenge.utils.Applicationutils;
import com.digicorechallenge.test.helper.ServiceTestHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class AuthenticationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserAccountService accountService;

	@MockBean
	private JWTUtil jwtUtil;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@Test
	void testLogin() throws JsonProcessingException, Exception {
		// given
		AuthRequest request = ServiceTestHelper.makeAuthRequest();
		// when
		when(accountService.findByUsername(any())).thenReturn(Optional.of(ServiceTestHelper.createAuthUser()));
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		when(jwtUtil.generateToken(any())).thenReturn("testing");

		mockMvc.perform(post(Applicationutils.API_VERSION + "auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk());

	}

}
