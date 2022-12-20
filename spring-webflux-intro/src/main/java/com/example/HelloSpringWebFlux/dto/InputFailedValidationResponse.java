package com.example.HelloSpringWebFlux.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InputFailedValidationResponse {

	private int errorCode;
	private int code;
	private String message;
}
