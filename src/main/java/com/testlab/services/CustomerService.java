package com.testlab.services;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.security.core.Authentication;

import com.testlab.DTO.AddressRequestDTO;
import com.testlab.DTO.CustomerRequestDTO;
import com.testlab.DTO.CustomerResponseDTO;
import com.testlab.DTO.CustomerUpdateDTO;

public interface CustomerService {

    CustomerResponseDTO createCustomer(CustomerRequestDTO dto);

    CustomerResponseDTO getCustomerById(Long id,Authentication auth);

    List<CustomerResponseDTO> getAllCustomers(Authentication auth);

    CustomerResponseDTO updateCustomer(Long id, CustomerUpdateDTO dto,Authentication auth);

    void deleteCustomer(Long id,Authentication auth);

    CustomerResponseDTO addOrUpdateAddress(Long customerId, AddressRequestDTO dto,Authentication auth);
}
