package com.testlab.services;

import java.util.List;

import com.testlab.DTO.AccountRequestDTO;
import com.testlab.DTO.AccountResponseDTO;

public interface AccountService {

    AccountResponseDTO createAccount(Long customerId, AccountRequestDTO requestDTO);

    AccountResponseDTO getAccountById(Long id);

    List<AccountResponseDTO> getAllAccounts();

    AccountResponseDTO updateAccount(Long id, AccountRequestDTO requestDTO);

    void deleteAccount(Long id);
}
