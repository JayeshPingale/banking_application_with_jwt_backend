package com.testlab.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassbookResponseDTO {
	@JsonIgnore
	private Long accountId;
	private String accountNumber;
	private Double currentBalance;
	private TransactionResponseDTO transaction;
}
