package com.digicore.challenge.controller;

import static com.digicore.challenge.enums.ResponseCode.INVALID_CREDENTIALS;
import static com.digicore.challenge.enums.ResponseCode.NOT_FOUND;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digicore.challenge.dto.request.AuthRequest;
import com.digicore.challenge.dto.response.AuthResponse;
import com.digicore.challenge.dto.response.ServiceResponse;
import com.digicore.challenge.exception.ServiceException;
import com.digicore.challenge.security.JWTUtil;
import com.digicore.challenge.services.UserAccountService;
import com.digicore.challenge.utils.Applicationutils;
import com.digicore.challenge.utils.ResponseBuilder;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(Applicationutils.API_VERSION + "auth/")
@AllArgsConstructor
public class AuthenticationController {

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserAccountService accountService;

	@PostMapping("login")
	@ApiOperation(value = "login endpoint")
	public ResponseEntity<ServiceResponse> login(@Valid @RequestBody AuthRequest request) {
		return accountService.findByUsername(request.getAccountNumber()).map(user -> {
			if (passwordEncoder.matches(request.getPassword(), user.getAccountPassword())) {
				return ResponseEntity.ok(
						ResponseBuilder.buildSuccessfulResponse(new AuthResponse(true, jwtUtil.generateToken(user))));
			} else {
				throw new ServiceException(Integer.valueOf(INVALID_CREDENTIALS.getCanonicalCode()),
						INVALID_CREDENTIALS.getDescription(), LocalDateTime.now().toString());
			}
		}).orElseThrow(() -> new ServiceException(Integer.valueOf(NOT_FOUND.getCanonicalCode()),
				NOT_FOUND.getDescription(), LocalDateTime.now().toString()));
	}
}
