package com.testlab.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassbookRequestDTO {

//	@Id
//	@NotNull
//	private Long customerId;
	
	@NotNull
	private String accountNumber;
	
}
