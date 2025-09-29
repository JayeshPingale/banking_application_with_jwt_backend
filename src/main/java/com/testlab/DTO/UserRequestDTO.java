package com.testlab.DTO;


import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Data
public class UserRequestDTO {

	@Size(min=2)
	@Column(nullable=false)
	private String userName;
	
	@NotBlank
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$", message = "Password must be 8-20 characters, with upper, lower, digit, and special character")
	private String password;
	
	@NotBlank
	@Size(min=2)
	@Column(nullable=false)
	@Pattern(regexp = "^(?i)(ROLE_ADMIN|ROLE_CUSTOMER)$", message = "Role name must be either 'admin' or 'customer'")
	private String roleName;
	
	@Valid
	private AddressRequestDTO address;

	@Valid
	private CustomerRequestDTO customer;
}

