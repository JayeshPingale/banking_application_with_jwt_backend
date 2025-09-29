package com.testlab.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.testlab.DTO.PassbookResponseDTO;
import com.testlab.DTO.TransactionResponseDTO;
import com.testlab.entities.Passbook;
import com.testlab.entities.Transaction;

@Component
public class PassbookMapper {
	
	private ModelMapper modelMapper;

	public PassbookMapper(ModelMapper modelMapper) {
		super();
		this.modelMapper = modelMapper;
	}
	

	public PassbookResponseDTO toResponse(Passbook passbook, Transaction transaction) {
        PassbookResponseDTO passrepdto = modelMapper.map(passbook, PassbookResponseDTO.class);
        TransactionResponseDTO txnDTO = modelMapper.map(transaction, TransactionResponseDTO.class);
        txnDTO.setFromaccountId(passbook.getAccountId());
        txnDTO.setBalance(passbook.getBalance());
        passrepdto.setTransaction(txnDTO);
        passrepdto.setCurrentBalance(passbook.getBalance());
        return passrepdto;
    }
}
