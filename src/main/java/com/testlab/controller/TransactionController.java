package com.testlab.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testlab.DTO.TransactionRequestDTO;
import com.testlab.DTO.TransactionResponseDTO;
import com.testlab.services.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Valid @RequestBody TransactionRequestDTO dto) {
        return ResponseEntity.ok(transactionService.createTransaction(dto));
    }
//    @PostMapping
//    public ResponseEntity<TransactionResponseDTO> createTransaction(
//            @RequestBody TransactionRequestDTO dto) {
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User loggedInUser = (User) auth.getPrincipal();  // fetch logged-in user
//        String email = loggedInUser.getCustomer().getEmailid(); // get customer email
//
//        TransactionResponseDTO response = transactionService.createTransaction(dto, email);
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_CUSTOMER') and #customerId == authentication.principal.id)")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(transactionService.getTransactionsByCustomerId(customerId));
    }
}
