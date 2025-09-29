package com.testlab.mapper;

import com.testlab.DTO.TransactionRequestDTO;
import com.testlab.DTO.TransactionResponseDTO;
import com.testlab.entities.Transaction;

public class TransactionMapper {

    public static Transaction toEntity(TransactionRequestDTO req) {
        if (req == null) return null;
        Transaction txn = new Transaction();
        txn.setAmount(req.getAmount());
        txn.setTransType(req.getTransType());
//        txn.setTimestamp(req.getTimestamp());
        
        return txn;
    }

//    public static TransactionResponseDTO toResponse(Transaction t) {
//        if (t == null) return null;
//        TransactionResponseDTO resp = new TransactionResponseDTO();
//        resp.setTransId(t.getTransId());
//        resp.setAmount(t.getAmount());
//        resp.setTransType(t.getTransType());
////        resp.setTimestamp(t.getTimestamp());
////        resp.setAccountId(t.getAccount().getId());
//        return resp;
//    }
    
    public static TransactionResponseDTO toResponse(Transaction txn) {
        TransactionResponseDTO resp = new TransactionResponseDTO();
        resp.setTransId(txn.getTransId());
        resp.setTransType(txn.getTransType());
        resp.setAmount(txn.getAmount());
        resp.setDate(txn.getDate());

        if (txn.getFromAccount() != null) {
            resp.setFromaccountId(txn.getFromAccount().getAccountId());
            resp.setFromAccountNumber(txn.getFromAccount().getAccountNumber()); // NEW
            resp.setBalance(txn.getFromAccount().getBalance());
        } else {
            resp.setFromaccountId(null);
            resp.setFromAccountNumber(null); // NEW
            resp.setBalance(null);
        }

        if (txn.getToAccount() != null) {
            resp.setToaccountId(txn.getToAccount().getAccountId());
            resp.setToAccountNumber(txn.getToAccount().getAccountNumber()); // NEW
        } else {
            resp.setToaccountId(null);
            resp.setToAccountNumber(null); // NEW
        }

        if (txn.getCustomer() != null) {
            resp.setCustomerId(txn.getCustomer().getCustomerId());
        } else {
            resp.setCustomerId(null);
        }

        return resp;
    }


}
