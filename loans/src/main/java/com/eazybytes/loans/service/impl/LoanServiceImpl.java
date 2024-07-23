package com.eazybytes.loans.service.impl;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.eazybytes.loans.constants.LoansConstants;
import com.eazybytes.loans.dto.LoanDto;
import com.eazybytes.loans.entity.Loan;
import com.eazybytes.loans.exception.LoanAlreadyExistsException;
import com.eazybytes.loans.exception.ResourceNotFoundException;
import com.eazybytes.loans.mapper.LoanMapper;
import com.eazybytes.loans.repository.LoanRepository;
import com.eazybytes.loans.service.ILoanService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements ILoanService {
	private final LoanRepository loanRepository;

	@Override
	public void createLoan(String mobileNumber) {
		Optional<Loan> optionalLoan = loanRepository.findByMobileNumber(mobileNumber);
		optionalLoan.ifPresent(c -> {
			throw new LoanAlreadyExistsException("Loanalready exists with given mobile number: " + c.getMobileNumber());
		});

		loanRepository.save(createNewLoan(mobileNumber));
	}

	@Override
	public LoanDto fetchLoan(String mobileNumber) {
		Loan loan = loanRepository.findByMobileNumber(mobileNumber)
			.orElseThrow(() -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber));
		return LoanMapper.mapToCardDto(loan, new LoanDto());
	}

	@Override
	public boolean updateLoan(LoanDto loanDto) {
		Loan loan = loanRepository.findByLoanNumber(loanDto.getLoanNumber())
			.orElseThrow(() -> new ResourceNotFoundException("Loan", "loanNumber", loanDto.getLoanNumber()));
		LoanMapper.mapToCard(loanDto, loan);
		loanRepository.save(loan);
		return true;
	}

	@Override
	@Transactional
	public boolean deleteLoan(String mobileNumber) {
		Loan loan = loanRepository.findByMobileNumber(mobileNumber)
			.orElseThrow(() -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber));
		loanRepository.deleteById(loan.getLoanId());
		return true;
	}

	private Loan createNewLoan(String mobileNumber) {
		Loan newLoan = new Loan();
		long randomLoanNumber = 100_000_000_000L + new Random().nextInt(900_000_000);
		newLoan.setLoanNumber(Long.toString(randomLoanNumber));
		newLoan.setMobileNumber(mobileNumber);
		newLoan.setLoanType(LoansConstants.HOME_LOAN);
		newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
		newLoan.setAmountPaid(0);
		newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
		return newLoan;
	}

}
