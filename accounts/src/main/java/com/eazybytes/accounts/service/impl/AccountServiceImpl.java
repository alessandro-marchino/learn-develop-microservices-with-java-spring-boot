package com.eazybytes.accounts.service.impl;

import org.springframework.stereotype.Service;

import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.repository.AccountRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.IAccountService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {
	private final CustomerRepository customerRepository;
	private final AccountRepository accountRepository;

	@Override
	public void createAccount(CustomerDto customerDto) {
		
	}

}
