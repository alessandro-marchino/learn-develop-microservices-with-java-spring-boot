package com.eazybytes.accounts.service;

import com.eazybytes.accounts.dto.CustomerDetailDto;

public interface ICustomerService {

	CustomerDetailDto fetchCustomerDetail(String mobileNumber, String correlationId);
}
