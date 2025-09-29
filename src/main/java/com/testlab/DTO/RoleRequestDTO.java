package com.testlab.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleRequestDTO {
	
	@NotBlank
	@Size(min=2)
	@Column(nullable=false)
	@Pattern(regexp = "^(?i)(ROlE_ADMIN|ROLE_CUSTOMER)$", message = "Role name must be either 'admin' or 'customer'")
	private String roleName;

}
