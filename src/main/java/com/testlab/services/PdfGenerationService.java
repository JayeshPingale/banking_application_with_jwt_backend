package com.testlab.services;

import java.util.List;

import com.testlab.entities.Transaction;

public interface PdfGenerationService {
	  byte[] generatePassbookPdf(String accountNumber, Double currentBalance, List<Transaction> transactions);
	
}
