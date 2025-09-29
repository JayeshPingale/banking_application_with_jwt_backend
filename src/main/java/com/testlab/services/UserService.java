package com.testlab.services;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.testlab.DTO.UserRequestDTO;
import com.testlab.DTO.UserResponseDTO;
import com.testlab.DTO.UserUpdateDTO;

public interface UserService {
	 UserResponseDTO create(UserRequestDTO reqDTO);
	 
		
	 UserResponseDTO getUserByID(Long id,Authentication auth);
		
		List<UserResponseDTO> getAllUsers(Authentication auth);
		
		UserResponseDTO updateUser(Long id,UserUpdateDTO req,Authentication auth);
		
		void deleteUserbyID(Long id,Authentication auth);
		
		UserResponseDTO updatePassword(Long userId, String newPassword,Authentication auth);
		
}
