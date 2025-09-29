package com.testlab.entities;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Data
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountId;

	@NotNull
	@Pattern(regexp = "^\\d{12}$", message = "Account number must be exactly 12 digits")
	@Column(name = "account_number", nullable = false, unique = true)
	private String accountNumber;

	@NotBlank
	@Pattern(regexp = "^(SAVINGS|CURRENT|SALARY|FD)$", message = "Invalid account type")
	private String accountType;

	@NotNull
	@DecimalMin(value = "500.00", message = "Minimum balance must be ₹500.00")
	private Double balance;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "customer_id", nullable = false)
	@JsonManagedReference 
	private Customer customer;

	@OneToMany(mappedBy = "fromAccount", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
	private List<Transaction> fromTransactions = new ArrayList<>();

	@OneToMany(mappedBy = "toAccount", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
	private List<Transaction> toTransactions = new ArrayList<>();

	
	

}
