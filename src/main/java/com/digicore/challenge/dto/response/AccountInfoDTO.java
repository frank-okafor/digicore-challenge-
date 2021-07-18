package com.digicore.challenge.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountInfoDTO {
	private String accountName;
	private BigDecimal totalBalance;
	private String accountNumber;
}
