package com.testlab.services;

import java.util.List;

import com.testlab.DTO.TransactionRequestDTO;
import com.testlab.DTO.TransactionResponseDTO;

public interface TransactionService {

//    TransactionResponseDTO createTransaction(TransactionRequestDTO dto, String loggedInEmail);

	TransactionResponseDTO createTransaction(TransactionRequestDTO dto);

	TransactionResponseDTO getTransactionById(Long transId);

	List<TransactionResponseDTO> getAllTransactions();

	List<TransactionResponseDTO> getTransactionsByCustomerId(Long customerId);
}
