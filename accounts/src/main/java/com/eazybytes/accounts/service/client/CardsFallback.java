package com.eazybytes.accounts.service.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.eazybytes.accounts.dto.CardDto;

@Component
public class CardsFallback implements CardsFeignClient {

	@Override
	public ResponseEntity<CardDto> fetchCardDetails(String correlazionId, String mobileNumber) {
		return null;
	}

}
