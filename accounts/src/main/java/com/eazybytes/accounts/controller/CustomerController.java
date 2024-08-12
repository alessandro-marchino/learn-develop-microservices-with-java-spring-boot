package com.eazybytes.accounts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eazybytes.accounts.dto.CustomerDetailDto;
import com.eazybytes.accounts.dto.ErrorResponseDto;
import com.eazybytes.accounts.service.ICustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
@Tag(name = "REST APIs for Customers in EazyBank", description = "REST APIs in EazyBank to FETCH customer details")
@Slf4j
public class CustomerController {

	private final ICustomerService customerService;

	@Operation(summary = "Fetch Customer Details REST API", description = "REST API to fetch Customer details based on a mobile number")
	@ApiResponse(responseCode = "200", description = "HTTP status OK")
	@ApiResponse(responseCode = "400", description = "HTTP status BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@GetMapping("/fetchCustomerDetails")
	public ResponseEntity<CustomerDetailDto> fetchCustomerDetails(
			@RequestHeader("eazybank-correlation-id") String correlazionId,
			@RequestParam @NotEmpty(message = "Account number cannot be null or empty") @Pattern(regexp = "^$|[0-9]{10}", message = "Account number must be 10 digits") String mobileNumber) {
		log.debug("EazyBank-correlation-id found: {}", correlazionId);
		CustomerDetailDto customerDetailDto = customerService.fetchCustomerDetail(mobileNumber, correlazionId);
		return ResponseEntity.status(HttpStatus.OK).body(customerDetailDto);
	}

}
