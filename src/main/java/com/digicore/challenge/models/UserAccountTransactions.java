package com.digicore.challenge.models;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountTransactions {

	private String transactionDate;
	private String transactionType;
	private String narration;
	private BigDecimal amount;
	private BigDecimal accountBalance;

}
