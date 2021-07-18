package com.digicore.challenge.utils;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Applicationutils {
	public static final String SUCCESSFULL_WITHDRAWAL = "your withdrawal was successful";
	public static final String SUCCESSFULL_DEPOSIT = "your account has been successfully credited";
	public static final String API_VERSION = "/api/v1/";
	private static final Random RANDOM = new SecureRandom();
	private static final String ALPHABET = "1234567890987654321";
	private static final int ACCOUNT_NUMBER_LIMIT = 10;

	public static final String[] RESOURCE_WHITELIST = { "/", "/swagger-ui.html", "/webjars/**", "/swagger-resources/**",
			"/v2/api-docs", "/info", API_VERSION + "auth/login", API_VERSION + "account/create_account" };

	public static String generateUniqueAccountNumber() {
		StringBuilder main = new StringBuilder(ACCOUNT_NUMBER_LIMIT);
		for (int i = 0; i < ACCOUNT_NUMBER_LIMIT; i++) {
			main.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		return main.toString();
	}

}
