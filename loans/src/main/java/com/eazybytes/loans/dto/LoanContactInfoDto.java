package com.eazybytes.loans.dto;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import io.swagger.v3.oas.annotations.media.Schema;

@ConfigurationProperties(prefix = "loans")
@Schema(name = "Loan", description = "Schema to hold Contact information")
public record LoanContactInfoDto(
		@Schema(description = "Loan contact information message", example = "Welcome")
		String message,
		@Schema(description = "Contact details")
		ContactDetailsDto contactDetails,
		@Schema(description = "On-call support numbers", example = "[ (000) 000-1234 ]")
		List<String> onCallSupport) {
	// Nothing
}

@Schema(name = "ContactDetails", description = "Schema to hold Contact Details information")
record ContactDetailsDto(
		@Schema(description = "Contact Name", example = "John Doe")
		String name,
		@Schema(description = "Contact Email", example = "john@example.com")
		String email) {
	// Nothing
}
