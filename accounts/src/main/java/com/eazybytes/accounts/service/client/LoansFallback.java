package com.eazybytes.accounts.service.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.eazybytes.accounts.dto.LoanDto;

@Component
public class LoansFallback implements LoansFeignClient {

	@Override
	public ResponseEntity<LoanDto> fetchLoanDetails(String correlazionId, String mobileNumber) {
		return null;
	}

}
