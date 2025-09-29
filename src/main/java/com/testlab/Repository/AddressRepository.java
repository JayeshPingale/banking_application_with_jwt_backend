package com.testlab.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testlab.entities.Address;

public interface AddressRepository  extends JpaRepository<Address, Long>{

}
