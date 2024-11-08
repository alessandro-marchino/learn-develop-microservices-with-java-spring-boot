package com.eazybytes.loans.controller;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eazybytes.common.dto.ErrorResponseDto;
import com.eazybytes.common.dto.ResponseDto;
import com.eazybytes.loans.constants.LoansConstants;
import com.eazybytes.loans.dto.LoanContactInfoDto;
import com.eazybytes.loans.dto.LoanDto;
import com.eazybytes.loans.service.ILoanService;

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
@Tag(name = "CRUD REST APIs for Loans in EazyBank", description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH and DELETE loan details")
@Slf4j
public class LoanController {

	private final ILoanService loanService;
	private final Environment environment;
	private final LoanContactInfoDto loanContactInfo;

	@Value("${build.version:unknown}")
	private String buildVersion;

	@Operation(summary = "Create Loan REST API", description = "REST API to create new Loan inside EazyBank")
	@ApiResponse(responseCode = "201", description = "HTTP status CREATED")
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@PostMapping("/create")
	public ResponseEntity<ResponseDto> createLoan(
			@Valid @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
		loanService.createLoan(mobileNumber);
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(new ResponseDto(LoansConstants.STATUS_201, LoansConstants.MESSAGE_201));
	}

	@Operation(summary = "Fetch Loan Details REST API", description = "REST API to fetch Loan details based on a mobile number")
	@ApiResponse(responseCode = "200", description = "HTTP status OK")
	@ApiResponse(responseCode = "400", description = "HTTP status BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@GetMapping("/fetch")
	public ResponseEntity<LoanDto> fetchLoanDetails(
			@RequestHeader("eazybank-correlation-id") String correlazionId,
			@RequestParam @NotEmpty(message = "Account number cannot be null or empty") @Pattern(regexp = "^$|[0-9]{10}", message = "Account number must be 10 digits") String mobileNumber) {
		log.debug("fetchLoanDetails method start");
		LoanDto loanDto = loanService.fetchLoan(mobileNumber);
		log.debug("fetchLoanDetails method end");
		return ResponseEntity.status(HttpStatus.OK).body(loanDto);
	}

	@Operation(summary = "Update Loan Details REST API", description = "REST API to update Loan details based on a mobile number")
	@ApiResponse(responseCode = "200", description = "HTTP status OK")
	@ApiResponse(responseCode = "417", description = "HTTP status EXPECTATION FAILED", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
	@ApiResponse(responseCode = "400", description = "HTTP status BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@PutMapping("/update")
	public ResponseEntity<ResponseDto> updateAccountDetails(
			@Valid @RequestBody LoanDto LoanDto) {
		boolean isUpdated = loanService.updateLoan(LoanDto);
		if (isUpdated) {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
		}
		return ResponseEntity
				.status(HttpStatus.EXPECTATION_FAILED)
				.body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_UPDATE));
	}

	@Operation(summary = "Delete Loan Details REST API", description = "REST API to delete Loan details based on a mobile number")
	@ApiResponse(responseCode = "200", description = "HTTP status OK")
	@ApiResponse(responseCode = "417", description = "HTTP status EXPECTATION FAILED", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
	@ApiResponse(responseCode = "400", description = "HTTP status BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@DeleteMapping("/delete")
	public ResponseEntity<ResponseDto> deleteLoanDetails(
			@RequestParam @NotEmpty(message = "Account number cannot be null or empty") @Pattern(regexp = "^$|[0-9]{10}", message = "Account number must be 10 digits") String mobileNumber) {
		boolean isDeleted = loanService.deleteLoan(mobileNumber);
		if (isDeleted) {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
		}
		return ResponseEntity
				.status(HttpStatus.EXPECTATION_FAILED)
				.body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_DELETE));
	}

	@Operation(summary = "Get build information", description = "Get build information that is deployed into accounts microservice")
	@ApiResponse(responseCode = "200", description = "HTTP status OK")
	@ApiResponse(responseCode = "400", description = "HTTP status BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@GetMapping("/build-info")
	public ResponseEntity<String> getBuildInfo() {
		return ResponseEntity.ok().body(buildVersion);
	}

	@Operation(summary = "Get java version", description = "Get java version details that is deployed into accounts microservice")
	@ApiResponse(responseCode = "200", description = "HTTP status OK")
	@ApiResponse(responseCode = "400", description = "HTTP status BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@GetMapping("/java-version")
	public ResponseEntity<String> getJavaVersion() {
		return ResponseEntity.ok().body(environment.getProperty("JAVA_HOME", "unknown"));
	}

	@Operation(summary = "Get contact info", description = "Contact info details that can be reached out in case of any issues")
	@ApiResponse(responseCode = "200", description = "HTTP status OK", content = @Content(schema = @Schema(implementation = LoanContactInfoDto.class)))
	@ApiResponse(responseCode = "400", description = "HTTP status BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@GetMapping("/contact-info")
	public ResponseEntity<LoanContactInfoDto> getContactInfo() {
		log.debug("Invoked Loans contact-info API");
		return ResponseEntity.ok().body(loanContactInfo);
	}
}
