package com.digicore.challenge.dto.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO {
	@NotNull(message = "account number cannot be null")
	@NotBlank(message = "account number must be provided")
	private String accountNumber;
	private String accountPassword;
	@NotNull(message = "amount Cannot be null")
	private BigDecimal amount;
	private String narration = "";

}
