package com.eazybytes.accounts.function;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eazybytes.accounts.service.IAccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AccountsFunction {

	@Bean
	Consumer<Long> updateCommunication(IAccountService accountService) {
		return accountNumber -> {
			log.info("Updating Communication status for the account number: {}", accountNumber);
			accountService.updateCommunicationStatus(accountNumber);
		};
	}
}
