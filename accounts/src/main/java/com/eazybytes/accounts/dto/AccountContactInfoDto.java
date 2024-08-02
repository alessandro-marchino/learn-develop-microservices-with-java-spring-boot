package com.eazybytes.accounts.dto;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@ConfigurationProperties(prefix = "accounts")
@Schema(name = "Account", description = "Schema to hold Contact information")
@Data
public class AccountContactInfoDto {
	@Schema(description = "Account contact information message", example = "Welcome")
	private String message;
	@Schema(description = "Contact details")
	private ContactDetailsDto contactDetails;
	@Schema(description = "On-call support numbers", example = "[ (000) 000-1234 ]")
	private List<String> onCallSupport;
}

@Schema(name = "ContactDetails", description = "Schema to hold Contact Details information")
@Data
class ContactDetailsDto {
	@Schema(description = "Contact Name", example = "John Doe")
	private String name;
	@Schema(description = "Contact Email", example = "john@example.com")
	private String email;
}
