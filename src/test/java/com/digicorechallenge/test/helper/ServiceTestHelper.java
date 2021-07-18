package com.digicorechallenge.test.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.digicore.challenge.dto.request.AccountRequest;
import com.digicore.challenge.dto.request.AuthRequest;
import com.digicore.challenge.dto.request.TransactionRequestDTO;
import com.digicore.challenge.models.User;
import com.digicore.challenge.models.UserAccount;
import com.digicore.challenge.models.UserAccountTransactions;
import com.digicore.challenge.utils.Applicationutils;
import com.github.javafaker.Faker;

public class ServiceTestHelper {
	static final Faker faker = Faker.instance();

	public static List<UserAccountTransactions> transactonList() {
		return new ArrayList<>(Arrays.asList(
				new UserAccountTransactions("2020-07-14", "credit", "testing", new BigDecimal(200),
						new BigDecimal(5000)),
				new UserAccountTransactions("2020-07-14", "debit", "testing", new BigDecimal(200),
						new BigDecimal(5000))));

	}

	public static UserAccount userToCreate() {
		UserAccount user = new UserAccount(faker.name().firstName(), Applicationutils.generateUniqueAccountNumber(),
				faker.name().username(), new BigDecimal(200), new BigDecimal(5000), new Date(), transactonList());
		return user;
	}

	public static AccountRequest createAccountRequest() {
		return new AccountRequest(faker.name().firstName(), faker.name().username(), new BigDecimal(2000));
	}

	public static TransactionRequestDTO createTransactionRequest() {
		return new TransactionRequestDTO("11223344554", faker.name().username(), new BigDecimal(2000), "testing");
	}

	public static User createAuthUser() {
		return new User("11223344554", faker.name().username(), faker.name().firstName());
	}

	public static AuthRequest makeAuthRequest() {
		return new AuthRequest("11223344554", faker.name().username());
	}

}
