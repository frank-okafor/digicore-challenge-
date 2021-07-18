package com.digicore.challenge.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount {

	@NotNull
	private String accountName;

	@Pattern(regexp = "(\\+)?[0-9]{10}$", message = "account number must be between 10 digits")
	private String accountNumber;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotNull
	private String accountPassword;

	private BigDecimal initialDeposit;

	private BigDecimal totalBalance;

	private Date createdDate;

	private List<UserAccountTransactions> transactions = new ArrayList<>();

}