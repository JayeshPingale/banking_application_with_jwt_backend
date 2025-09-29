package com.testlab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.testlab.DTO.PassbookRequestDTO;
import com.testlab.DTO.PassbookResponseDTO;
import com.testlab.services.PassbookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/passbook")
public class PassbookController {

    @Autowired
    private PassbookService passbookService;

    @PostMapping("/generate")
    public ResponseEntity<PassbookResponseDTO> generatePassbook(@Valid @RequestBody PassbookRequestDTO requestDTO) {
        PassbookResponseDTO response = passbookService.generateAndSendPassbook(requestDTO.getAccountNumber());
        return ResponseEntity.ok(response);
    }
}
