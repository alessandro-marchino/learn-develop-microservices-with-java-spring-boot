package com.eazybytes.accounts.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.eazybytes.accounts.dto.LoanDto;

@FeignClient(name = "loans", url = "http://loans:8090", fallback = LoansFallback.class)
public interface LoansFeignClient {

	@GetMapping(value = "/api/fetch", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<LoanDto> fetchLoanDetails(@RequestHeader("eazybank-correlation-id") String correlazionId, @RequestParam String mobileNumber);
}
