package com.digicore.challenge.services;

import static com.digicore.challenge.enums.ResponseCode.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.digicore.challenge.dto.request.AccountRequest;
import com.digicore.challenge.dto.request.TransactionRequestDTO;
import com.digicore.challenge.dto.response.ServiceResponse;
import com.digicore.challenge.exception.ServiceException;
import com.digicore.challenge.models.UserAccount;
import com.digicore.challenge.repository.AccountRepository;
import com.digicorechallenge.test.helper.ServiceTestHelper;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class UserAccountServiceTest {
	@Mock
	private PasswordEncoder encoder;
	@Mock
	private AccountRepository accountRepository;

	private UserAccountService underTest;

	@BeforeEach
	void setUp() throws Exception {
		underTest = new UserAccountService(encoder, accountRepository);
	}

	@Test
	void testCreateAccount() {
		// given
		AccountRequest accountRequest = ServiceTestHelper.createAccountRequest();
		// when
		when(accountRepository.checkIfAccountNameExists(anyString())).thenReturn(false);
		when(accountRepository.save(any())).thenReturn(ServiceTestHelper.userToCreate());
		ServiceResponse response = underTest.createAccount(accountRequest);
		// then
		verify(accountRepository).save(any(UserAccount.class));
		verify(encoder, times(1)).encode(anyString());
		assertNotNull(response.getData());
	}

	@Test
	void testGetAccountInfo() {
		// when
		when(accountRepository.findByAccountNumber(anyString()))
				.thenReturn(Optional.of(ServiceTestHelper.userToCreate()));
		ServiceResponse response = underTest.getAccountInfo("testing");
		// then
		assertNotNull(response.getData());

	}

	@Test
	void testGetAccountStatement() {
		// when
		when(accountRepository.findByAccountNumber(anyString()))
				.thenReturn(Optional.of(ServiceTestHelper.userToCreate()));
		ServiceResponse response = underTest.getAccountStatement("testing");
		// then
		assertNotNull(response.getData());
	}

	@Test
	void testWhenAccountNumberDoesNotExist() {
		// when
		when(accountRepository.findByAccountNumber(anyString())).thenReturn(Optional.empty());
		// then
		assertThatThrownBy(() -> underTest.getAccountStatement("testing")).isInstanceOf(ServiceException.class)
				.hasMessageContaining(NOT_FOUND.getDescription());
	}

	@Test
	void testWithdraw() {
		// given
		TransactionRequestDTO request = ServiceTestHelper.createTransactionRequest();
		// when
		when(accountRepository.findByAccountNumber(anyString()))
				.thenReturn(Optional.of(ServiceTestHelper.userToCreate()));
		ServiceResponse response = underTest.withdraw(request);
		// then
		verify(accountRepository).update(any(UserAccount.class));
		assertNotNull(response.getData());
	}

	@Test
	void testDeposit() {
		// given
		TransactionRequestDTO request = ServiceTestHelper.createTransactionRequest();
		// when
		when(accountRepository.findByAccountNumber(anyString()))
				.thenReturn(Optional.of(ServiceTestHelper.userToCreate()));
		ServiceResponse response = underTest.deposit(request);
		// then
		verify(accountRepository).update(any(UserAccount.class));
		assertNotNull(response.getData());
	}

}
