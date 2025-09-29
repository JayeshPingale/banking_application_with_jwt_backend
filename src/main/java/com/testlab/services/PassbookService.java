package com.testlab.services;

import com.testlab.DTO.PassbookResponseDTO;

public interface PassbookService {
//	PassbookResponseDTO generateAndSendPassbook(PassbookRequestDTO requestDTO);
	
	PassbookResponseDTO generateAndSendPassbook(String accountNumber);


}
