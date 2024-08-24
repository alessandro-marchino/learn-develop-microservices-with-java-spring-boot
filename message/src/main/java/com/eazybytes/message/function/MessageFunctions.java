package com.eazybytes.message.function;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eazybytes.message.dto.AccountMessageDto;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MessageFunctions {

	@Bean
	Function<AccountMessageDto, AccountMessageDto> email() {
		return accountMessageDto -> {
			log.info("Sending email with the details: {}", accountMessageDto.toString());
			return accountMessageDto;
		};
	}

	@Bean
	Function<AccountMessageDto, Long> sms() {
		return accountMessageDto -> {
			log.info("Sending SMS with the details: {}", accountMessageDto.toString());
			return accountMessageDto.accountNumber();
		};
	}
}
