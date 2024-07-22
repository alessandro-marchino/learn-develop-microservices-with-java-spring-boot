package com.eazybytes.accounts.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "ErrorResponse", description = "Schema to hold error response information")
public class ErrorResponseDto {

	@Schema(description = "API path invoked by the client")
	private String apiPath;
	@Schema(description = "Error code representing the error that happened")
	private HttpStatus errorCode;
	@Schema(description = "Error message representing the error that happened")
	private String errorMessage;
	@Schema(description = "Time representing when the error happened")
	private LocalDateTime errorTime;
}
