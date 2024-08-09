package com.eazybytes.accounts.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eazybytes.accounts.dto.CardDto;

@FeignClient("cards")
public interface CardsFeignClient {

	@GetMapping(value = "/api/fetch", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<CardDto> fetchCardDetails(@RequestParam String mobileNumbe);
}
