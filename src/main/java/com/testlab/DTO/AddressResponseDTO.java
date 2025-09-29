package com.testlab.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data

public class AddressResponseDTO {
	@JsonIgnore
	private Long addressId;
	private String city;
	private String state;
	private String pincode;
}
