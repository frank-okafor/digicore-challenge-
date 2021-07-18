package com.digicore.challenge.services;

import static com.digicore.challenge.enums.ResponseCode.ALREADY_EXIST;
import static com.digicore.challenge.enums.ResponseCode.BAD_REQUEST;
import static com.digicore.challenge.enums.ResponseCode.INTERNAL_SERVER_ERROR;
import static com.digicore.challenge.enums.ResponseCode.NOT_FOUND;
import static com.digicore.challenge.enums.ResponseCode.OK;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.digicore.challenge.dto.request.AccountRequest;
import com.digicore.challenge.dto.request.TransactionRequestDTO;
import com.digicore.challenge.dto.response.AccountInfoDTO;
import com.digicore.challenge.dto.response.ServiceResponse;
import com.digicore.challenge.exception.ServiceException;
import com.digicore.challenge.models.User;
import com.digicore.challenge.models.UserAccount;
import com.digicore.challenge.models.UserAccountTransactions;
import com.digicore.challenge.repository.AccountRepository;
import com.digicore.challenge.utils.Applicationutils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserAccountService {

	private @Autowired PasswordEncoder passwordEncoder;
	private @Autowired AccountRepository accountRepository;
	final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Optional<User> findByUsername(String username) {
		return accountRepository.findByAccountNumber(username).map(account -> {
			User user = new User(account.getAccountNumber(), account.getAccountPassword(), account.getAccountName());
			return Optional.of(user);
		}).orElseGet(() -> Optional.empty());

	}

	public ServiceResponse createAccount(AccountRequest accountRequest) {
		ServiceResponse response;

		if (accountRepository.checkIfAccountNameExists(accountRequest.getAccountName())) {
			throw new ServiceException(Integer.valueOf(ALREADY_EXIST.getCanonicalCode()),
					"Account with name '" + accountRequest.getAccountName() + "' already exists",
					LocalDateTime.now().toString());
		}

		if (accountRequest.getInitialDeposit() == null
				|| accountRequest.getInitialDeposit().compareTo(new BigDecimal("500.00")) < 0) {
			throw new ServiceException(Integer.valueOf(BAD_REQUEST.getCanonicalCode()),
					"Minimum initial deposit amount is #500", LocalDateTime.now().toString());
		}

		UserAccount account = UserAccount.builder().accountName(accountRequest.getAccountName())
				.initialDeposit(accountRequest.getInitialDeposit())
				.accountPassword(passwordEncoder.encode(accountRequest.getAccountPassword()))
				.accountNumber(alwaysGetUniqueAccountNumber()).createdDate(new Date()).transactions(new ArrayList<>())
				.totalBalance(accountRequest.getInitialDeposit()).build();

		try {
			response = new ServiceResponse(OK.getCanonicalCode(), OK.getDescription(), LocalDateTime.now().toString(),
					accountRepository.save(account));
		} catch (Exception e) {
			log.error("Exception occurred while creating account {}", e.getMessage());
			return new ServiceResponse(INTERNAL_SERVER_ERROR.getCanonicalCode(), INTERNAL_SERVER_ERROR.getDescription(),
					LocalDateTime.now().toString(), null);
		}

		return response;
	}

	public ServiceResponse getAccountInfo(String accountNumber) {
		return accountRepository.findByAccountNumber(accountNumber).map(account -> {
			AccountInfoDTO response = AccountInfoDTO.builder().accountName(account.getAccountName())
					.accountNumber(account.getAccountNumber()).totalBalance(account.getTotalBalance()).build();
			return new ServiceResponse(OK.getCanonicalCode(), OK.getDescription(), LocalDateTime.now().toString(),
					response);
		}).orElseThrow(() -> new ServiceException(Integer.valueOf(NOT_FOUND.getCanonicalCode()),
				NOT_FOUND.getDescription(), LocalDateTime.now().toString()));
	}

	public ServiceResponse getAccountStatement(String accountNumber) {
		return accountRepository.findByAccountNumber(accountNumber).map(account -> {
			return new ServiceResponse(OK.getCanonicalCode(), OK.getDescription(), LocalDateTime.now().toString(),
					account.getTransactions());
		}).orElseThrow(() -> new ServiceException(Integer.valueOf(NOT_FOUND.getCanonicalCode()),
				NOT_FOUND.getDescription(), LocalDateTime.now().toString()));
	}

	public ServiceResponse withdraw(TransactionRequestDTO request) {
		return accountRepository.findByAccountNumber(request.getAccountNumber()).map(user -> {
			if (request.getAmount() == null || request.getAmount().compareTo(new BigDecimal("1.00")) < 0) {
				throw new ServiceException(Integer.valueOf(BAD_REQUEST.getCanonicalCode()),
						"Mininum allowed withdrawal #1.00", LocalDateTime.now().toString());
			}

			if (user.getTotalBalance().subtract(request.getAmount()).compareTo(new BigDecimal("500.00")) < 0) {
				throw new ServiceException(Integer.valueOf(BAD_REQUEST.getCanonicalCode()),
						"You cannot withdraw the specified amount as minimum account balance is #500",
						LocalDateTime.now().toString());
			}

			user.setTotalBalance(user.getTotalBalance().subtract(request.getAmount()));
			user.getTransactions()
					.add(UserAccountTransactions.builder().amount(request.getAmount()).narration(request.getNarration())
							.transactionType("Debit").transactionDate(formatter.format(new Date()))
							.accountBalance(user.getTotalBalance()).build());
			accountRepository.update(user);
			return new ServiceResponse(OK.getCanonicalCode(), OK.getDescription(), LocalDateTime.now().toString(),
					Applicationutils.SUCCESSFULL_WITHDRAWAL);
		}).orElseThrow(() -> new ServiceException(Integer.valueOf(NOT_FOUND.getCanonicalCode()),
				NOT_FOUND.getDescription(), LocalDateTime.now().toString()));
	}

	public ServiceResponse deposit(TransactionRequestDTO request) {
		return accountRepository.findByAccountNumber(request.getAccountNumber()).map(user -> {
			if (request.getAmount().compareTo(new BigDecimal("1.00")) <= 0) {
				throw new ServiceException(Integer.valueOf(BAD_REQUEST.getCanonicalCode()),
						"Minimum deposit amount is #1.00", LocalDateTime.now().toString());
			}

			if (request.getAmount() == null || request.getAmount().compareTo(new BigDecimal("1000000")) > 0) {
				throw new ServiceException(Integer.valueOf(BAD_REQUEST.getCanonicalCode()),
						"Maximum deposit amount is #1,000,000", LocalDateTime.now().toString());
			}

			user.setTotalBalance(user.getTotalBalance().add(request.getAmount()));
			user.getTransactions()
					.add(UserAccountTransactions.builder().amount(request.getAmount()).narration(request.getNarration())
							.transactionType("Credit").transactionDate(formatter.format(new Date()))
							.accountBalance(user.getTotalBalance()).build());
			accountRepository.update(user);
			return new ServiceResponse(OK.getCanonicalCode(), OK.getDescription(), LocalDateTime.now().toString(),
					Applicationutils.SUCCESSFULL_DEPOSIT);
		}).orElseThrow(() -> new ServiceException(Integer.valueOf(NOT_FOUND.getCanonicalCode()),
				NOT_FOUND.getDescription(), LocalDateTime.now().toString()));
	}

	private String alwaysGetUniqueAccountNumber() {
		String accountNumber = "";
		for (;;) {
			accountNumber = Applicationutils.generateUniqueAccountNumber();
			if (!accountRepository.existsByAccountNumber(accountNumber)) {
				break;
			}
		}
		return accountNumber;
	}

}
