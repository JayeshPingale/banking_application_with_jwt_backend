package com.testlab.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.testlab.DTO.AccountRequestDTO;
import com.testlab.DTO.AccountResponseDTO;
import com.testlab.services.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // Create account for a customer
    @PostMapping("/customer/{customerId}")
    public ResponseEntity<AccountResponseDTO> createAccount(
            @PathVariable Long customerId,
            @Valid @RequestBody AccountRequestDTO dto) {

        AccountResponseDTO resp = accountService.createAccount(customerId, dto);
        return ResponseEntity.created(URI.create("/api/accounts/" + resp.getAccountId())).body(resp);
    }

    // Get account by id
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    // Get all accounts
    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    // Update account
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody AccountRequestDTO dto) {
        return ResponseEntity.ok(accountService.updateAccount(id, dto));
    }

    // Delete account
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok("Account deleted successfully!");
    }
}
