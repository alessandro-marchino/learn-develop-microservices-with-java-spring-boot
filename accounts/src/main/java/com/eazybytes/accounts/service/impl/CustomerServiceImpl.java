package com.eazybytes.accounts.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eazybytes.accounts.dto.AccountDto;
import com.eazybytes.accounts.dto.CardDto;
import com.eazybytes.accounts.dto.CustomerDetailDto;
import com.eazybytes.accounts.dto.LoanDto;
import com.eazybytes.accounts.entity.Account;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.repository.AccountRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.ICustomerService;
import com.eazybytes.accounts.service.client.CardsFeignClient;
import com.eazybytes.accounts.service.client.LoansFeignClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {
	
	private final AccountRepository accountRepository;
	private final CustomerRepository customerRepository;
	private final CardsFeignClient cardsFeignClient;
	private final LoansFeignClient loansFeignClient;
	
	@Override
	public CustomerDetailDto fetchCustomerDetail(String mobileNumber, String correlationId) {
		Customer customer = customerRepository.findByMobileNumber(mobileNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
		Account account = accountRepository.findByCustomerId(customer.getCustomerId())
				.orElseThrow(() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId()));
		CustomerDetailDto customerDetailDto = CustomerMapper.mapToCustomerDetailDto(customer, new CustomerDetailDto());
		customerDetailDto.setAccountDto(AccountMapper.mapToAccountDto(account, new AccountDto()));
		
		ResponseEntity<LoanDto> loanDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationId, mobileNumber);
		customerDetailDto.setLoanDto(loanDtoResponseEntity.getBody());
		
		ResponseEntity<CardDto> cardDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
		customerDetailDto.setCardDto(cardDtoResponseEntity.getBody());
		
		return customerDetailDto;
	}

}
