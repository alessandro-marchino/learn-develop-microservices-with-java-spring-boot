package com.eazybytes.cards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
	info = @Info(
		title = "Cards microservice REST API documentation",
		description = "EazyBank Cards microservice REST API documentation",
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
		description = "EazyBank Cards microservice REST API documentation",
		url = "https://www.eazybytes.com/swagger-ui.html"
	)
)
public class CardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardsApplication.class, args);
	}

}