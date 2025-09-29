package com.testlab.services;

import com.testlab.DTO.UserResponseDTO;
import com.testlab.DTO.security.LoginDto;
import com.testlab.DTO.security.RegistrationDto;

public interface AuthService {
	
	UserResponseDTO register(RegistrationDto registration);
    String login(LoginDto loginDto);

}
