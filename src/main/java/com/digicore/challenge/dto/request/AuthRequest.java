package com.digicore.challenge.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
	@NotNull(message = "account number cannot be null")
	@NotBlank(message = "account number must be provided")
	private String accountNumber;
	@NotNull(message = "password cannot be null")
	@NotBlank(message = "password must be provided")
	private String password;
}
