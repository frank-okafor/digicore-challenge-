package com.digicore.challenge.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.digicore.challenge.models.UserAccount;
import com.digicorechallenge.test.helper.ServiceTestHelper;

class AccountRepositoryTest {
	private AccountRepository accountRepository;

	@BeforeEach
	void setUp() throws Exception {
		accountRepository = new AccountRepository();
		accountRepository.removeAll();
	}

	@Test
	void testExistsByAccountNumber() {
		// given
		UserAccount user = ServiceTestHelper.userToCreate();
		// when
		accountRepository.save(user);
		// then
		assertThat(accountRepository.existsByAccountNumber(user.getAccountNumber())).isEqualTo(true);
	}

	@Test
	void testFindByAccountNumber() {
		// given
		UserAccount user = ServiceTestHelper.userToCreate();
		// when
		accountRepository.save(user);
		// then
		assertThat(accountRepository.findByAccountNumber(user.getAccountNumber()).isPresent()).isEqualTo(true);
	}

	@Test
	void testCheckIfAccountNameExists() {
		// given
		UserAccount user = ServiceTestHelper.userToCreate();
		// when
		accountRepository.save(user);
		// then
		assertThat(accountRepository.checkIfAccountNameExists(user.getAccountName())).isEqualTo(true);
	}

}
