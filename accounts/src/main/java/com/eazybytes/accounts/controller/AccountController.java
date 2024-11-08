package com.eazybytes.accounts.controller;

import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.AccountContactInfoDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.service.IAccountService;
import com.eazybytes.common.dto.ErrorResponseDto;
import com.eazybytes.common.dto.ResponseDto;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
@Tag(name = "CRUD REST APIs for Accounts in EazyBank", description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH and DELETE account details")
@Slf4j
public class AccountController {

	private final IAccountService accountService;
	private final Environment environment;
	private final AccountContactInfoDto accountContactInfo;

	@Value("${build.version:unknown}")
	private String buildVersion;

	@Operation(summary = "Create Account REST API", description = "REST API to create new Customer & Account inside EazyBank")
	@ApiResponse(responseCode = "201", description = "HTTP status CREATED")
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@PostMapping("/create")
	public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
		accountService.createAccount(customerDto);
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
	}

	@Operation(summary = "Fetch Account Details REST API", description = "REST API to fetch Customer & Account details based on a mobile number")
	@ApiResponse(responseCode = "200", description = "HTTP status OK")
	@ApiResponse(responseCode = "400", description = "HTTP status BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@GetMapping("/fetch")
	public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam @NotEmpty(message = "Account number cannot be null or empty") @Pattern(regexp = "^$|[0-9]{10}", message = "Account number must be 10 digits") String mobileNumber) {
		CustomerDto customerDto = accountService.fetchAccount(mobileNumber);
		return ResponseEntity.status(HttpStatus.OK).body(customerDto);
	}

	@Operation(summary = "Update Account Details REST API", description = "REST API to update Customer & Account details based on a mobile number")
	@ApiResponse(responseCode = "200", description = "HTTP status OK")
	@ApiResponse(responseCode = "417", description = "HTTP status EXPECTATION FAILED", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
	@ApiResponse(responseCode = "400", description = "HTTP status BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@PutMapping("/update")
	public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
		boolean isUpdated = accountService.updateAccount(customerDto);
		if (isUpdated) {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
		}
		return ResponseEntity
				.status(HttpStatus.EXPECTATION_FAILED)
				.body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
	}

	@Operation(summary = "Delete Account Details REST API", description = "REST API to delete Customer & Account details based on a mobile number")
	@ApiResponse(responseCode = "200", description = "HTTP status OK")
	@ApiResponse(responseCode = "417", description = "HTTP status EXPECTATION FAILED", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
	@ApiResponse(responseCode = "400", description = "HTTP status BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@DeleteMapping("/delete")
	public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam @NotEmpty(message = "Account number cannot be null or empty") @Pattern(regexp = "^$|[0-9]{10}", message = "Account number must be 10 digits") String mobileNumber) {
		boolean isDeleted = accountService.deleteAccount(mobileNumber);
		if (isDeleted) {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
		}
		return ResponseEntity
				.status(HttpStatus.EXPECTATION_FAILED)
				.body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
	}

	@Operation(summary = "Get build information", description = "Get build information that is deployed into accounts microservice")
	@ApiResponse(responseCode = "200", description = "HTTP status OK")
	@ApiResponse(responseCode = "400", description = "HTTP status BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@GetMapping("/build-info")
	@Retry(name="getBuildInfo", fallbackMethod = "getBuildInfoFallback")
	public ResponseEntity<String> getBuildInfo() throws TimeoutException {
		log.debug("getBuildInfo() method invoked");
		return ResponseEntity.ok().body(buildVersion);
	}

	public ResponseEntity<String> getBuildInfoFallback(Throwable throwable) {
		log.debug("getBuildInfoFallback() method invoked. Exception: {}", throwable.getMessage());
		return ResponseEntity.ok().body("0.9");
	}

	@Operation(summary = "Get java version", description = "Get java version details that is deployed into accounts microservice")
	@ApiResponse(responseCode = "200", description = "HTTP status OK")
	@ApiResponse(responseCode = "400", description = "HTTP status BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@GetMapping("/java-version")
	@RateLimiter(name = "getJavaVersion", fallbackMethod = "getJavaVersionFallback")
	public ResponseEntity<String> getJavaVersion() {
		return ResponseEntity.ok().body(environment.getProperty("JAVA_HOME", "unknown"));
	}

	public ResponseEntity<String> getJavaVersionFallback(Throwable throwable) {
		return ResponseEntity.ok().body("Java 17");
	}

	@Operation(summary = "Get contact info", description = "Contact info details that can be reached out in case of any issues")
	@ApiResponse(responseCode = "200", description = "HTTP status OK", content = @Content(schema = @Schema(implementation = AccountContactInfoDto.class)))
	@ApiResponse(responseCode = "400", description = "HTTP status BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@GetMapping("/contact-info")
	public ResponseEntity<AccountContactInfoDto> getContactInfo() {
		return ResponseEntity.ok().body(accountContactInfo);
	}
}
