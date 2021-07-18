package com.digicore.challenge.dto.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {

    @NotNull(message = "account name Cannot be null")
    @NotBlank(message = "account name must be provided")
    private String accountName;
    @NotNull(message = "password Cannot be null")
    @NotBlank(message = "password must be provided")
    private String accountPassword;
    @NotNull(message = "initial deposit Cannot be null")
    private BigDecimal initialDeposit;

}
