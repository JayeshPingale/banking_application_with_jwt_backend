package com.testlab.DTO;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class TransactionResponseDTO {

    private Long transId;
    private String transType;
    private Double amount;
    private LocalDate date;
	@JsonIgnore
    private Long fromaccountId;
	@JsonIgnore
	private Long toaccountId;
	@JsonIgnore
	private Long customerId;
    private Double balance;
    private String fromAccountNumber;   // NEW
    private String toAccountNumber;     // NEW
}