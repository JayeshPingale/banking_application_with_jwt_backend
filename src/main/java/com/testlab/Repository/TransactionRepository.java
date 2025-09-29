package com.testlab.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testlab.entities.Account;
import com.testlab.entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	Optional<Account> findByCustomerCustomerId(Long customerId);
//    // Customer ke andar 'customerid' hai, isliye ye likhna hoga
//    List<Transaction> findByCustomerCustomerid(Integer customerid);
//
//    // Account ke andar 'accountId' hai, isliye ye likhna hoga
//    List<Transaction> findByAccountAccountId(Integer accountId);


}
