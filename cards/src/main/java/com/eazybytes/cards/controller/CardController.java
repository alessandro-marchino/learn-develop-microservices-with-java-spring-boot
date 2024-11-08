package com.eazybytes.cards.controller;

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

import com.eazybytes.cards.constants.CardsConstants;
import com.eazybytes.cards.dto.CardContactInfoDto;
import com.eazybytes.cards.dto.CardDto;
import com.eazybytes.cards.service.ICardService;
import com.eazybytes.common.dto.ErrorResponseDto;
import com.eazybytes.common.dto.ResponseDto;

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
@Tag(name = "CRUD REST APIs for Cards in EazyBank", description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH and DELETE card details")
@Slf4j
public class CardController {

	private final ICardService cardService;
	private final Environment environment;
	private final CardContactInfoDto cardContactInfo;

	@Value("${build.version:unknown}")
	private String buildVersion;

	@Operation(summary = "Create Card REST API", description = "REST API to create new Card inside EazyBank")
	@ApiResponse(responseCode = "201", description = "HTTP status CREATED")
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@PostMapping("/create")
	public ResponseEntity<ResponseDto> createCard(@Valid @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
		cardService.createCard(mobileNumber);
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(new ResponseDto(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201));
	}

	@Operation(summary = "Fetch Card Details REST API", description = "REST API to fetch Card details based on a mobile number")
	@ApiResponse(responseCode = "200", description = "HTTP status OK")
	@ApiResponse(responseCode = "400", description = "HTTP status BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@GetMapping("/fetch")
	public ResponseEntity<CardDto> fetchCardDetails(
			@RequestHeader("eazybank-correlation-id") String correlazionId,
			@RequestParam @NotEmpty(message = "Account number cannot be null or empty") @Pattern(regexp = "^$|[0-9]{10}", message = "Account number must be 10 digits") String mobileNumber) {
		log.debug("fetchCardDetails method start");
		CardDto cardDto = cardService.fetchCard(mobileNumber);
		log.debug("fetchCardDetails method end");
		return ResponseEntity.status(HttpStatus.OK).body(cardDto);
	}

	@Operation(summary = "Update Card Details REST API", description = "REST API to update Card details based on a mobile number")
	@ApiResponse(responseCode = "200", description = "HTTP status OK")
	@ApiResponse(responseCode = "417", description = "HTTP status EXPECTATION FAILED", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
	@ApiResponse(responseCode = "400", description = "HTTP status BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@PutMapping("/update")
	public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CardDto cardDto) {
		boolean isUpdated = cardService.updateCard(cardDto);
		if (isUpdated) {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
		}
		return ResponseEntity
				.status(HttpStatus.EXPECTATION_FAILED)
				.body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_UPDATE));
	}

	@Operation(summary = "Delete Card Details REST API", description = "REST API to delete Card details based on a mobile number")
	@ApiResponse(responseCode = "200", description = "HTTP status OK")
	@ApiResponse(responseCode = "417", description = "HTTP status EXPECTATION FAILED", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
	@ApiResponse(responseCode = "400", description = "HTTP status BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@DeleteMapping("/delete")
	public ResponseEntity<ResponseDto> deleteCardDetails(@RequestParam @NotEmpty(message = "Account number cannot be null or empty") @Pattern(regexp = "^$|[0-9]{10}", message = "Account number must be 10 digits") String mobileNumber) {
		boolean isDeleted = cardService.deleteCard(mobileNumber);
		if (isDeleted) {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
		}
		return ResponseEntity
				.status(HttpStatus.EXPECTATION_FAILED)
				.body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_DELETE));
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
	@ApiResponse(responseCode = "200", description = "HTTP status OK", content = @Content(schema = @Schema(implementation = CardContactInfoDto.class)))
	@ApiResponse(responseCode = "400", description = "HTTP status BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "HTTP status INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	@GetMapping("/contact-info")
	public ResponseEntity<CardContactInfoDto> getConcactInfo() {
		return ResponseEntity.ok().body(cardContactInfo);
	}
}
