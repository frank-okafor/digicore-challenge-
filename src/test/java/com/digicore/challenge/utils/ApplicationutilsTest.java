package com.digicore.challenge.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class ApplicationutilsTest {

	@Test
	void testGenerateUniqueAccountNumber() {
		// when
		String accountNumber = Applicationutils.generateUniqueAccountNumber();
		// then
		assertThat(accountNumber.length()).isEqualTo(10);
		assertThat(Pattern.matches("[a-zA-Z]+", accountNumber)).isEqualTo(false);
	}

}
