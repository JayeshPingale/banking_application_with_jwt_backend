
package com.testlab.DTO;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDTO {
	@NotBlank
	@Email
	@Column(nullable=false)
	@JsonProperty("email")
	String email;
	
	@NotBlank
	@Size(min=10)
	@Column(nullable=false,length = 10)
	String contactNumber;
	
	@Past
	@NotNull
	LocalDate dob;
	
	@Valid
	AddressRequestDTO address;
	
	@Valid
	private AccountRequestDTO account;

	
}
