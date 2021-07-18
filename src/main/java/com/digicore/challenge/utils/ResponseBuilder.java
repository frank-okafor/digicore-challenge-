package com.digicore.challenge.utils;

import java.time.LocalDateTime;

import com.digicore.challenge.dto.response.ServiceResponse;
import com.digicore.challenge.enums.ResponseCode;

public class ResponseBuilder {

	public static ServiceResponse buildSuccessfulResponse(Object data) {
		return ServiceResponse.builder().statusCode(ResponseCode.OK.getCanonicalCode())
				.statusMessage(ResponseCode.OK.getDescription()).timestamp(LocalDateTime.now().toString()).data(data)
				.build();

	}
}
