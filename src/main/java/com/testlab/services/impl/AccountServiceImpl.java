package com.testlab.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.testlab.DTO.AccountRequestDTO;
import com.testlab.DTO.AccountResponseDTO;
import com.testlab.Exception.NotFoundException;
import com.testlab.Repository.AccountRepository;
import com.testlab.Repository.CustomerRepository;
import com.testlab.entities.Account;
import com.testlab.entities.Customer;
import com.testlab.mapper.AccountMapper;
import com.testlab.services.AccountService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepo;
    private final CustomerRepository customerRepo;

    @Override
    @Transactional
    public AccountResponseDTO createAccount(Long customerId, AccountRequestDTO dto) {
//        Customer customer = customerRepo.findById(customerId)
//                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + customerId));
//
//        Account account = AccountMapper.toEntity(requestDTO);
//        account.setCustomer(customer);
//
//        Account savedAccount = accountRepo.save(account);
//        return AccountMapper.toResponse(savedAccount);
    	
    	 // Fetch customer
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        // Check if customer already has the account number
        boolean exists = customer.getAccounts().stream()
                .anyMatch(acc -> acc.getAccountNumber().equals(dto.getAccountNumber()));

        if (exists) {
            throw new IllegalArgumentException("This account number already exists. Please enter a different account number.");
        }

        // Create and save account
        Account account = new Account();
        account.setAccountNumber(dto.getAccountNumber());
        account.setAccountType(dto.getAccountType());
        account.setBalance(dto.getBalance());
        account.setCustomer(customer);

        customer.getAccounts().add(account);
        accountRepo.save(account);

        // Map to response
        AccountResponseDTO resp = new AccountResponseDTO();
        resp.setAccountId(account.getAccountId());
        resp.setAccountNumber(account.getAccountNumber());
        resp.setAccountType(account.getAccountType());
        resp.setBalance(account.getBalance());
        resp.setCustomerId(customer.getCustomerId());

        return resp;
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponseDTO getAccountById(Long id) {
        Account account = accountRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found: " + id));
        return AccountMapper.toResponse(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getAllAccounts() {
        return accountRepo.findAll().stream().map(AccountMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public AccountResponseDTO updateAccount(Long id, AccountRequestDTO requestDTO) {
        Account account = accountRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found: " + id));

        account.setAccountNumber(requestDTO.getAccountNumber());
        account.setAccountType(requestDTO.getAccountType());
        account.setBalance(requestDTO.getBalance());

        Account updated = accountRepo.save(account);
        return AccountMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteAccount(Long id) {
        if (!accountRepo.existsById(id)) {
            throw new NotFoundException("Account not found: " + id);
        }
        accountRepo.deleteById(id);
    }
}
