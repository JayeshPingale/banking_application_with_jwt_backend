package com.testlab.DTO;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
@Data

public class CustomerResponseDTO {
	@JsonIgnore
	private Long customerId;
	private String emailid;
	private String contactNo;
	private LocalDate dob;
	
	private AddressResponseDTO address;
	
//	private AccountResponseDTO account;

	private List<AccountResponseDTO> accounts;

}
