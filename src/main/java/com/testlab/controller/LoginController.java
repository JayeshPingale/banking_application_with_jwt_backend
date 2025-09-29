package com.testlab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testlab.DTO.*;
import com.testlab.DTO.security.JwtAuthResponse;
import com.testlab.DTO.security.LoginDto;
import com.testlab.DTO.security.RegistrationDto;
import com.testlab.services.AuthService;

@RestController
@RequestMapping("/api")
public class LoginController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/register")
	public ResponseEntity<UserResponseDTO> register(@RequestBody RegistrationDto registerDto)
	{
		return ResponseEntity.ok(authService.register(registerDto));
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto)
	{
		
		String token=authService.login(loginDto);
		JwtAuthResponse jwtAuthResponse=new JwtAuthResponse();
		jwtAuthResponse.setAccessToken(token);
		
		return ResponseEntity.ok(jwtAuthResponse);
	}

}
