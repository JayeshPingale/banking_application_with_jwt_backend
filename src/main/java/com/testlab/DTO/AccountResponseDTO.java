package com.testlab.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class AccountResponseDTO {
	@JsonIgnore
    private Long accountId;
    private String accountNumber;
    private String accountType;
    private Double balance;
	@JsonIgnore
    private Long customerId;  
}
