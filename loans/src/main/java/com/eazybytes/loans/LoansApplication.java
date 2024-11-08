package com.eazybytes.loans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.eazybytes.loans.dto.LoanContactInfoDto;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties(value = LoanContactInfoDto.class)
@OpenAPIDefinition(
	info = @Info(
		title = "Accounts microservice REST API documentation",
		description = "EazyBank Loans microservice REST API documentation",
		version = "v1",
		contact = @Contact(
			name = "Madan Reddy",
			email = "tutor@eazybytes.com",
			url = "https://www.eazybytes.com"
		),
		license = @License(
			name = "Apache 2.0",
			url = "https://www.apache.org/licenses/LICENSE-2.0.txt",
			identifier = "Apache-2.0"
		)
	),
	externalDocs = @ExternalDocumentation(
		description = "EazyBank Loans microservice REST API documentation",
		url = "https://www.eazybytes.com/swagger-ui.html"
	)
)
public class LoansApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoansApplication.class, args);
	}

}
