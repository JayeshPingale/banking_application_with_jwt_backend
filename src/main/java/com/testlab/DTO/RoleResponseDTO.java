package com.testlab.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleResponseDTO {
	@JsonIgnore
	private Long roleId;
	private String roleName;
}
