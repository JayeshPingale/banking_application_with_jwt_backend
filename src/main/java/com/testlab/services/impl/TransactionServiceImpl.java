package com.testlab.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.testlab.DTO.TransactionRequestDTO;
import com.testlab.DTO.TransactionResponseDTO;
import com.testlab.Exception.NotFoundException;
import com.testlab.Repository.AccountRepository;
import com.testlab.Repository.CustomerRepository;
import com.testlab.Repository.TransactionRepository;
import com.testlab.entities.Account;
import com.testlab.entities.Customer;
import com.testlab.entities.Transaction;
import com.testlab.mapper.TransactionMapper;
import com.testlab.services.TransactionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepo;
	private final AccountRepository accountRepo;
	private final CustomerRepository customerRepo;
	@Autowired
	private JavaMailSender mailSender;

//	@Override
//	public TransactionResponseDTO createTransaction(TransactionRequestDTO dto) {
//		// Fetch accounts and customer
//		Account fromAccount = accountRepo.findByAccountNumber(dto.getFromaccountId())
//				.orElseThrow(() -> new NotFoundException("From Account not found: " + dto.getFromaccountId()));
//
//		if ("TRANSFER".equals(dto.getTransType()) && dto.getToaccountId() == null) {
//		    throw new IllegalArgumentException("toAccountId is required for TRANSFER");
//		}
//		Account toAccount = null;
//		if ("TRANSFER".equals(dto.getTransType())) {
//			if (dto.getToaccountId() == null) {
//				throw new NotFoundException("to Account id is required for TRANSFER");
//			}
//			toAccount = accountRepo.findById(dto.getToaccountId())
//					.orElseThrow(() -> new NotFoundException("To Account not found"));
//		}
//		Customer customer = customerRepo.findById(dto.getCustomerId())
//				.orElseThrow(() -> new NotFoundException("Customer not found: " + dto.getCustomerId()));
//
//		if (dto.getTransType().equals("DEBIT") || dto.getTransType().equals("TRANSFER")) {
//			Double balance = fromAccount.getBalance() - dto.getAmount();
//			if (balance < 500) {
//				throw new IllegalArgumentException("Insuffient balance 500 is the minimum balance");
//			}
//		}
//
//		switch (dto.getTransType()) {
//		case "DEBIT" -> fromAccount.setBalance(fromAccount.getBalance() - dto.getAmount());
//		case "CREDIT" -> fromAccount.setBalance(fromAccount.getBalance() + dto.getAmount());
//		case "TRANSFER" -> {
//			fromAccount.setBalance(fromAccount.getBalance() - dto.getAmount());
//			toAccount.setBalance(fromAccount.getBalance() + dto.getAmount());
//		}
//
//		default -> {
//			throw new IllegalArgumentException("Invalid transaction type: " + dto.getTransType());
//		}
//		}
//		// Save accounts
//		accountRepo.save(fromAccount);
//		if (toAccount != null) {
//			accountRepo.save(toAccount);
//		}
//
//		// Create Transaction entity
//		Transaction txn = new Transaction();
//		txn.setFromAccount(fromAccount);
////		txn.setToAccount(toAccount);  // null allowed for CREDIT/DEBIT
//		txn.setToAccount(dto.getTransType().equals("TRANSFER") ? toAccount : null);
//		txn.setCustomer(customer);
//		txn.setTransType(dto.getTransType());
//		txn.setAmount(dto.getAmount());
//		txn.setDate(LocalDateTime.now());
//
//		// Save transaction
//		Transaction savedTxn = transactionRepo.save(txn);
//
//		// Map to Response DTO using TransactionMapper
//		return TransactionMapper.toResponse(savedTxn);
//	}
	
	@Override
	public TransactionResponseDTO createTransaction(TransactionRequestDTO dto) {
		 
		
		Account fromAccount = accountRepo.findByAccountNumber(dto.getFromAccountNumber())
		        .orElseThrow(() -> new NotFoundException("From Account not found: " + dto.getFromAccountNumber()));

		Account toAccount = null;
		if ("TRANSFER".equals(dto.getTransType())) {
		    if (dto.getToAccountNumber() == null) {
		        throw new NotFoundException("To Account number is required for TRANSFER");
		    }
		    toAccount = accountRepo.findByAccountNumber(dto.getToAccountNumber())
		    		.orElseThrow(() -> new NotFoundException("To Account not found: " + dto.getToAccountNumber()));
		}

	    Customer customer = customerRepo.findById(dto.getCustomerId())
	            .orElseThrow(() -> new NotFoundException("Customer not found: " + dto.getCustomerId()));


//		    Customer customer = customerRepo.findByEmailid(dt)
//		            .orElseThrow(() -> new NotFoundException("Customer not found for logged-in user"));
//
//		    Account fromAccount = accountRepo.findByAccountNumber(dto.getFromAccountNumber())
//		            .orElseThrow(() -> new NotFoundException("From Account not found: " + dto.getFromAccountNumber()));
//
//		    if (!fromAccount.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
//		        throw new IllegalArgumentException("You are not authorized to transact from this account!");
//		    }
//
//		    Account toAccount = null;
		    if ("TRANSFER".equals(dto.getTransType())) {
		        toAccount = accountRepo.findByAccountNumber(dto.getToAccountNumber())
		                .orElseThrow(() -> new NotFoundException("To Account not found: " + dto.getToAccountNumber()));
		    }
	    // Check balance for DEBIT or TRANSFER
	    if (dto.getTransType().equals("DEBIT") || dto.getTransType().equals("TRANSFER")) {
	        Double balance = fromAccount.getBalance() - dto.getAmount();
	        if (balance < 500) {
	            throw new IllegalArgumentException("Insufficient balance. Minimum balance is 500.");
	        }
	    }

	    // Update account balances
	    switch (dto.getTransType()) {
	        case "DEBIT" -> fromAccount.setBalance(fromAccount.getBalance() - dto.getAmount());
	        case "CREDIT" -> fromAccount.setBalance(fromAccount.getBalance() + dto.getAmount());
	        case "TRANSFER" -> {
	            fromAccount.setBalance(fromAccount.getBalance() - dto.getAmount());
	            toAccount.setBalance(toAccount.getBalance() + dto.getAmount());
	        }
	        default -> throw new IllegalArgumentException("Invalid transaction type: " + dto.getTransType());
	    }

	    // Save accounts
	    accountRepo.save(fromAccount);
	    if (toAccount != null) accountRepo.save(toAccount);

	    // Create and save transaction
	    Transaction txn = new Transaction();
	    txn.setFromAccount(fromAccount);
	    txn.setToAccount(toAccount);
	    txn.setCustomer(customer);
	    txn.setTransType(dto.getTransType());
	    txn.setAmount(dto.getAmount());
	    txn.setDate(LocalDate.now());
	    Transaction savedTxn = transactionRepo.save(txn);

	    // Send email notifications
	    try {
	        // Always send to fromAccount owner
	        sendTransactionEmail(fromAccount.getCustomer().getEmailid(), savedTxn, fromAccount);

	        // For TRANSFER, also send to toAccount owner
	        if ("TRANSFER".equals(dto.getTransType())) {
	            sendTransactionEmail(toAccount.getCustomer().getEmailid(), savedTxn, toAccount);
	        }
	    } catch (Exception e) {
	        e.printStackTrace(); // log error, don’t fail transaction
	    }

	    // Map to response DTO
	    return TransactionMapper.toResponse(savedTxn);
	}

//	@Override
//	public TransactionResponseDTO createTransaction(TransactionRequestDTO dto, String loggedInEmail) {
//	    // Get customer based on logged-in user
//	    Customer customer = customerRepo.findByEmailid(loggedInEmail)
//	            .orElseThrow(() -> new NotFoundException("Customer not found for logged-in user"));
//
//	    // Fetch fromAccount based on provided fromAccountNumber
//	    Account fromAccount = accountRepo.findByAccountNumber(dto.getFromAccountNumber())
//	            .orElseThrow(() -> new NotFoundException("From Account not found: " + dto.getFromAccountNumber()));
//
//	    // Check if fromAccount belongs to logged-in customer
//	    if (!fromAccount.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
//	        throw new IllegalArgumentException("You are not authorized to perform transactions from this account");
//	    }
//
//	    Account toAccount = null;
//	    if ("TRANSFER".equals(dto.getTransType())) {
//	        if (dto.getToAccountNumber() == null) {
//	            throw new NotFoundException("To Account number is required for TRANSFER");
//	        }
//	        toAccount = accountRepo.findByAccountNumber(dto.getToAccountNumber())
//	                .orElseThrow(() -> new NotFoundException("To Account not found: " + dto.getToAccountNumber()));
//	    }
//
//	    // Check balance for DEBIT or TRANSFER
//	    if (dto.getTransType().equals("DEBIT") || dto.getTransType().equals("TRANSFER")) {
//	        Double balance = fromAccount.getBalance() - dto.getAmount();
//	        if (balance < 500) {
//	            throw new IllegalArgumentException("Insufficient balance. Minimum balance is 500.");
//	        }
//	    }
//
//	    // Update account balances
//	    switch (dto.getTransType()) {
//	        case "DEBIT" -> fromAccount.setBalance(fromAccount.getBalance() - dto.getAmount());
//	        case "CREDIT" -> fromAccount.setBalance(fromAccount.getBalance() + dto.getAmount());
//	        case "TRANSFER" -> {
//	            fromAccount.setBalance(fromAccount.getBalance() - dto.getAmount());
//	            toAccount.setBalance(toAccount.getBalance() + dto.getAmount());
//	        }
//	        default -> throw new IllegalArgumentException("Invalid transaction type: " + dto.getTransType());
//	    }
//
//	    // Save accounts
//	    accountRepo.save(fromAccount);
//	    if (toAccount != null) accountRepo.save(toAccount);
//
//	    // Create and save transaction
//	    Transaction txn = new Transaction();
//	    txn.setFromAccount(fromAccount);
//	    txn.setToAccount(toAccount);
//	    txn.setCustomer(customer);
//	    txn.setTransType(dto.getTransType());
//	    txn.setAmount(dto.getAmount());
//	    txn.setDate(LocalDate.now());
//	    Transaction savedTxn = transactionRepo.save(txn);
//
//	    // Send email notifications
//	    sendTransactionEmail(fromAccount.getCustomer().getEmailid(), savedTxn, fromAccount);
//	    if ("TRANSFER".equals(dto.getTransType())) {
//	        sendTransactionEmail(toAccount.getCustomer().getEmailid(), savedTxn, toAccount);
//	    }
//
//	    // Map to response DTO
//	    return TransactionMapper.toResponse(savedTxn);
//	}

	@Override
	@Transactional(readOnly = true)
	public TransactionResponseDTO getTransactionById(Long transId) {
		Transaction transaction = transactionRepo.findById(transId)
				.orElseThrow(() -> new NotFoundException("Transaction not found: " + transId));

		TransactionResponseDTO resp = new TransactionResponseDTO();
		resp.setTransId(transaction.getTransId());
		resp.setTransType(transaction.getTransType());
		resp.setAmount(transaction.getAmount());
		resp.setDate(transaction.getDate());
		resp.setFromaccountId(transaction.getFromAccount().getAccountId());
		resp.setToaccountId(transaction.getToAccount().getAccountId());
		resp.setCustomerId(transaction.getCustomer().getCustomerId());

		return resp;
	}

	@Override
	public List<TransactionResponseDTO> getAllTransactions() {
	    List<Transaction> transactions = transactionRepo.findAll();
	    return transactions.stream()
	            .map(TransactionMapper::toResponse) // null-safe mapper
	            .collect(Collectors.toList());
	}
	@Override
	@Transactional(readOnly = true)
	public List<TransactionResponseDTO> getTransactionsByCustomerId(Long customerId) {
		Customer customer = customerRepo.findById(customerId)
				.orElseThrow(() -> new NotFoundException("CustomerId not found"));

		List<Transaction> transactions = customer.getTransactions();
		if (transactions == null || transactions.isEmpty()) {
			throw new NotFoundException("No transactions assigned to this customer.");
		}

		// Map each Transaction to TransactionResponseDTO
		return transactions.stream().map(tx -> {
			TransactionResponseDTO dto = new TransactionResponseDTO();
			dto.setTransId(tx.getTransId());
			dto.setTransType(tx.getTransType());
			dto.setAmount(tx.getAmount());
			dto.setDate(tx.getDate());
			dto.setFromaccountId(tx.getFromAccount() != null ? tx.getFromAccount().getAccountId() : null);
			dto.setToaccountId(tx.getToAccount() != null ? tx.getToAccount().getAccountId() : null);
			dto.setCustomerId(tx.getCustomer() != null ? tx.getCustomer().getCustomerId() : null);
			return dto;
		}).toList();
	}

	
//	public void sendTransactionEmail(String toEmail, Transaction transaction, Account account) {
//	    String subject = "Transaction Alert: " + transaction.getTransType();
//	    
//	    StringBuilder body = new StringBuilder();
//	    body.append("Dear Customer Welcome to ,\n\n");
//	    body.append("A transaction has been made on your account.\n");
//	    body.append("Account Number: ").append(account.getAccountNumber()).append("\n");
//	    body.append("Transaction ID: ").append(transaction.getTransId()).append("\n");
//	    body.append("Type: ").append(transaction.getTransType()).append("\n");
//	    body.append("Amount: ₹").append(transaction.getAmount()).append("\n");
//	    body.append("Balance After Transaction: ₹").append(account.getBalance()).append("\n");
//	    body.append("Date: ").append(transaction.getDate()).append("\n\n");
//	    body.append("Thank you for banking with us.");
//
//	    SimpleMailMessage message = new SimpleMailMessage();
//	    message.setTo(toEmail);
//	    message.setSubject(subject);
//	    message.setText(body.toString());
//
//	    mailSender.send(message);
//	}
	
	public void sendTransactionEmail(String toEmail, Transaction transaction, Account account) {
	    if (toEmail != null && !toEmail.isEmpty()) {
	        String subject = "💳 Transaction Alert: " + transaction.getTransType() + " on your account";
	        
	        String text = "Dear Customer,\n\n"
	                + "Welcome to Lena Bank! 🏦\n\n"
	                + "A new transaction has been processed on your account.\n\n"
	                + "Account Number: " + account.getAccountNumber() + "\n"
	                + "Transaction ID: " + transaction.getTransId() + "\n"
	                + "Transaction Type: " + transaction.getTransType() + "\n"
	                + "Amount: ₹" + transaction.getAmount() + "\n"
	                + "Balance After Transaction: ₹" + account.getBalance() + "\n"
	                + "Date: " + transaction.getDate() + "\n\n"
	                + "Please review your account and contact us if you notice any discrepancies.\n\n"
	                + "Thank you for banking with us!\n"
	                + "— The Lena Bank Team";

	        try {
	            SimpleMailMessage message = new SimpleMailMessage();
	            message.setTo(toEmail);
	            message.setSubject(subject);
	            message.setText(text);

	            mailSender.send(message);
	        } catch (Exception e) {
	            System.err.println("Failed to send transaction email: " + e.getMessage());
	        }
	    }
	}

}
