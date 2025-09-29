package com.testlab.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Passbook {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long passbookId;
	
	@NotNull
	private Long accountId;
	
	@NotBlank
	@Pattern(regexp = "^\\d{12}$", message = "Account number must be exactly 12 digits")
    private String accountNumber;
	
	@NotNull
	@DecimalMin(value = "500.00", message = "Minimum balance must be ₹500.00")
	private Double balance;
	
	@NotNull
	private Long transId;
	
	@Column(nullable = false)
    @Pattern(regexp = "^(DEBIT|CREDIT|TRANSFER)$", message = "Invalid transaction type")
    private String transType; 
	
	@Column(nullable = false)
    private LocalDate date;
	

}
