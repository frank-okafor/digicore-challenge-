package com.digicore.challenge.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.digicore.challenge.dto.request.AccountRequest;
import com.digicore.challenge.dto.request.TransactionRequestDTO;
import com.digicore.challenge.repository.AccountRepository;
import com.digicore.challenge.services.UserAccountService;
import com.digicore.challenge.utils.Applicationutils;
import com.digicorechallenge.test.helper.ServiceTestHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class ServiceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserAccountService accountService;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@MockBean
	private AccountRepository accountRepository;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testCreateAccount() throws JsonProcessingException, Exception {
		// given
		AccountRequest request = ServiceTestHelper.createAccountRequest();
		// when
		when(accountRepository.checkIfAccountNameExists(any())).thenReturn(false);
		when(passwordEncoder.encode(anyString())).thenReturn("testing");
		when(accountRepository.save(any())).thenReturn(ServiceTestHelper.userToCreate());

		mockMvc.perform(post(Applicationutils.API_VERSION + "account/create_account")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "digicore", roles = { "USER,ADMIN" })
	void testGetAccountInfo() throws Exception {
		when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(ServiceTestHelper.userToCreate()));
		mockMvc.perform(get(Applicationutils.API_VERSION + "account/account_info/11223344322"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "digicore", roles = { "USER,ADMIN" })
	void testGetAccountStatement() throws Exception {
		when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(ServiceTestHelper.userToCreate()));
		mockMvc.perform(get(Applicationutils.API_VERSION + "account/account_statement/11223344322"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "digicore", roles = { "USER,ADMIN" })
	void testDeposit() throws JsonProcessingException, Exception {
		TransactionRequestDTO request = ServiceTestHelper.createTransactionRequest();
		when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(ServiceTestHelper.userToCreate()));
		when(accountRepository.update(any())).thenReturn(ServiceTestHelper.userToCreate());
		mockMvc.perform(post(Applicationutils.API_VERSION + "account/deposit").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "digicore", roles = { "USER,ADMIN" })
	void testWithdraw() throws JsonProcessingException, Exception {
		TransactionRequestDTO request = ServiceTestHelper.createTransactionRequest();
		when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(ServiceTestHelper.userToCreate()));
		when(accountRepository.update(any())).thenReturn(ServiceTestHelper.userToCreate());
		mockMvc.perform(post(Applicationutils.API_VERSION + "account/withdraw").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk());
	}

}
