package com.testlab.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testlab.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByEmailid(String username);

}
