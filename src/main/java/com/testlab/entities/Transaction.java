package com.testlab.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transId;

    @Column(nullable = false)
    @Pattern(regexp = "^(DEBIT|CREDIT|TRANSFER)$", message = "Invalid transaction type")
    private String transType; 

    @Column(nullable = false)
    @DecimalMin(value = "1.00", message = "Amount must be greater than 1")
    private Double amount;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;  

    @ManyToOne
    @JoinColumn(name = "to_account_id",nullable = true)
    private Account toAccount;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonManagedReference 
    private Customer customer;
}
