package com.testlab.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
@Data
public class CustomerUpdateDTO {
	@Email
	@Column(unique = true, nullable = false)
	private String email;


	@Pattern(regexp = "^[0-9]{10}$")
	private String contactNo;

	@Column(nullable = false)
	private String city;

	@Column(nullable = false)
	private String state;

	@Column(nullable = false, length = 6)
	private String pincode;

	@Pattern(regexp = "^(SAVINGS|CURRENT|SALARY|FD)$", message = "Invalid account type")
	private String accountType;

	
	@DecimalMin(value = "500.00", message = "Minimum balance must be ₹1000.00")
	private Double balance;




}
