package com.eazybytes.accounts.service.impl;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.AccountDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.entity.Account;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.exception.CustomerAlreadyExistsException;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.repository.AccountRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.IAccountService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {
	private final CustomerRepository customerRepository;
	private final AccountRepository accountRepository;

	@Override
	public void createAccount(CustomerDto customerDto) {
		Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
		optionalCustomer.ifPresent(c -> {
			throw new CustomerAlreadyExistsException("Customer already exists with given mobile number: " + c.getMobileNumber());
		});
		
		Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
		customer = customerRepository.save(customer);
		
		accountRepository.save(createNewAccount(customer));
	}
	
	@Override
	public CustomerDto fetchAccount(String mobileNumber) {
		Customer customer = customerRepository.findByMobileNumber(mobileNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
		Account account = accountRepository.findByCustomerId(customer.getCustomerId())
				.orElseThrow(() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId()));
		CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
		customerDto.setAccountDto(AccountMapper.mapToAccountDto(account, new AccountDto()));
		return customerDto;
	}

	@Override
	public boolean updateAccount(CustomerDto customerDto) {
		boolean isUpdated = false;
		AccountDto accountDto = customerDto.getAccountDto();
		if(accountDto != null) {
			Account account = accountRepository.findById(accountDto.getAccountNumber())
					.orElseThrow(() -> new ResourceNotFoundException("Account", "accountNumber", accountDto.getAccountNumber()));
			AccountMapper.mapToAccount(accountDto, account);
			accountRepository.save(account);
			
			Customer customer = customerRepository.findById(account.getCustomerId())
					.orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", account.getCustomerId()));
			CustomerMapper.mapToCustomer(customerDto, customer);
			customerRepository.save(customer);
			isUpdated = true;
		}
		return isUpdated;
	}

	@Override
	@Transactional
	public boolean deleteAccount(String mobileNumber) {
		Customer customer = customerRepository.findByMobileNumber(mobileNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
		accountRepository.deleteByCustomerId(customer.getCustomerId());
		customerRepository.deleteById(customer.getCustomerId());
		return true;
	}

	private Account createNewAccount(Customer customer) {
		Account account = new Account();
		account.setCustomerId(customer.getCustomerId());
		Long randomAccNumber = new Random().nextLong(10000000000L, 99999999999L);
		account.setAccountNumber(randomAccNumber);
		account.setAccountType(AccountsConstants.SAVINGS);
		account.setBranchAddress(AccountsConstants.ADDRESS);
		return account;
	}

}
