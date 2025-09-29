package com.testlab.mapper;

import com.testlab.DTO.AccountRequestDTO;
import com.testlab.DTO.AccountResponseDTO;
import com.testlab.entities.Account;

public class AccountMapper {

    // Convert DTO -> Entity
    public static Account toEntity(AccountRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Account account = new Account();
        account.setAccountNumber(dto.getAccountNumber());
        account.setAccountType(dto.getAccountType());
        account.setBalance(dto.getBalance());
        return account;
    }

    // Convert Entity -> ResponseDTO
    public static AccountResponseDTO toResponse(Account account) {
        if (account == null) {
            return null;
        }

        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setAccountId(account.getAccountId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());

        // Agar customer null nahi hai to uska id set kar dena
        if (account.getCustomer() != null) {
            dto.setCustomerId(account.getCustomer().getCustomerId());
        }

        return dto;
    }
}
