package com.digicore.challenge.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digicore.challenge.dto.request.AccountRequest;
import com.digicore.challenge.dto.request.TransactionRequestDTO;
import com.digicore.challenge.dto.response.ServiceResponse;
import com.digicore.challenge.services.UserAccountService;
import com.digicore.challenge.utils.Applicationutils;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(Applicationutils.API_VERSION + "account/")
@AllArgsConstructor
public class ServiceController {

	@Autowired
	private UserAccountService accountService;

	@PostMapping("create_account")
	@ApiOperation(value = "create account endpoint")
	public ResponseEntity<ServiceResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest) {
		return ResponseEntity.ok(accountService.createAccount(accountRequest));
	}

	@GetMapping("account_info/{accountNumber}")
	@ApiOperation(value = "get account information")
	public ResponseEntity<ServiceResponse> getAccountInfo(@PathVariable String accountNumber) {
		return ResponseEntity.ok(accountService.getAccountInfo(accountNumber));
	}

	@GetMapping("account_statement/{accountNumber}")
	@ApiOperation(value = "get account statement")
	public ResponseEntity<ServiceResponse> getAccountStatement(@PathVariable String accountNumber) {
		return ResponseEntity.ok(accountService.getAccountStatement(accountNumber));
	}

	@PostMapping("deposit")
	@ApiOperation(value = "credit account")
	public ResponseEntity<ServiceResponse> deposit(@Valid @RequestBody TransactionRequestDTO request) {
		return ResponseEntity.ok(accountService.deposit(request));
	}

	@PostMapping("withdraw")
	@ApiOperation(value = "debit account")
	public ResponseEntity<ServiceResponse> withdraw(@Valid @RequestBody TransactionRequestDTO request) {
		return ResponseEntity.ok(accountService.withdraw(request));
	}

}
