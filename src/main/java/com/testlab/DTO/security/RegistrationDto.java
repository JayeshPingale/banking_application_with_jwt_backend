package com.testlab.DTO.security;

import com.testlab.DTO.AddressRequestDTO;
import com.testlab.DTO.CustomerRequestDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDto {
	private String username;
	private String password;
	private String role;
	CustomerRequestDTO customer;
	private AddressRequestDTO address;


	
}
