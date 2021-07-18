package com.digicore.challenge.exception;

import static com.digicore.challenge.enums.ResponseCode.INTERNAL_SERVER_ERROR;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.digicore.challenge.dto.response.ServiceResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ResponseBody
@ControllerAdvice(annotations = RestController.class, basePackages = "com.reloadly.accountmicroservice.controllers")
public class AdviceControllerThrowable {

	@ExceptionHandler(NullPointerException.class)
	public ServiceResponse noAccessException(NullPointerException e) {
		log.error("Null Pointer exception", e);
		return new ServiceResponse(INTERNAL_SERVER_ERROR.getCanonicalCode(), INTERNAL_SERVER_ERROR.getDescription());
	}

	@ExceptionHandler(ServiceException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ServiceResponse jsonException(ServiceException e) {
		return new ServiceResponse(e.getHttpCode().toString(), e.getMessage());
	}

	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public ServiceResponse noAccessException(AuthenticationException e) {
		log.error("AuthenticationException ", e);
		return new ServiceResponse("401", e.getMessage() + ", you do not privilege to access this resource");
	}

	@ExceptionHandler(Exception.class)
	public ServiceResponse noAccessException(Exception e) {
		log.error("Unknown Exception", e);
		return new ServiceResponse(INTERNAL_SERVER_ERROR.getCanonicalCode(), INTERNAL_SERVER_ERROR.getDescription());
	}
}
