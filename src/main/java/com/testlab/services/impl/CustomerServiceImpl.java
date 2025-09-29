package com.testlab.services.impl;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.testlab.DTO.AddressRequestDTO;
import com.testlab.DTO.CustomerRequestDTO;
import com.testlab.DTO.CustomerResponseDTO;
import com.testlab.DTO.CustomerUpdateDTO;
import com.testlab.Exception.ResourceNotFoundException;
import com.testlab.Repository.CustomerRepository;
import com.testlab.Repository.UserRepository;
import com.testlab.entities.Address;
import com.testlab.entities.Customer;
import com.testlab.entities.User;
import com.testlab.mapper.CustomerMapper;
import com.testlab.services.CustomerService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService{
	 private final CustomerRepository customerRepo;
	 private final UserRepository userRepo;

	    @Override
	    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto) {
	        Address address = new Address();
	        address.setCity(dto.getAddress().getCity());
	        address.setState(dto.getAddress().getState());
	        address.setPincode(dto.getAddress().getPincode());

	        Customer customer = new Customer();
	        customer.setEmailid(dto.getEmail());
	        customer.setContactNumber(dto.getContactNumber());
	        customer.setDob(dto.getDob());
	        
	        // link address
	        customer.setAddress(address);

	        Customer savedCustomer = customerRepo.save(customer);

	        return CustomerMapper.toResponse(savedCustomer);
	    }

	    @Override
	    public CustomerResponseDTO getCustomerById(Long id,Authentication auth) {
	        Customer customer = customerRepo.findById(id)
	                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
	        return CustomerMapper.toResponse(customer);
	    }

	    @Override
	    public List<CustomerResponseDTO> getAllCustomers(Authentication auth) throws AccessDeniedException {
	
	    	
	    	User caller = userRepo.findByUserName(auth.getName())
	                .orElseThrow(() -> new ResourceNotFoundException("Caller user not found"));

	        boolean isAdmin = caller.getRoles().stream()
	                .anyMatch(r -> "ADMIN".equalsIgnoreCase(r.getRoleName()));

	        if (!isAdmin) 
	            {
	                throw new AccessDeniedException("You can only update your own customer details");
	            }
	        
	    	
	    	
	    	
	        return customerRepo.findAll().stream()
	                .map(CustomerMapper::toResponse)
	                .collect(Collectors.toList());
	    }

	    @Override
	    public CustomerResponseDTO updateCustomer(Long id, CustomerUpdateDTO dto,Authentication auth) {
	    	User caller=null;
			try {
				caller = userRepo.findByUserName(auth.getName())
				        .orElseThrow(() -> new Exception("Caller user not found"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        boolean isAdmin = caller.getRoles().stream()
	                .anyMatch(r -> "ROLE_ADMIN".equalsIgnoreCase(r.getRoleName()));

	        if (!isAdmin) {
	            Customer own = caller.getCustomer();
	            if (own == null || !own.getCustomerId().equals(id)) {
	                throw new AccessDeniedException("You can only update your own customer details");
	            }
	        }
	    	
	        Customer customer = customerRepo.findById(id)
	                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
	        CustomerMapper.updateEntity(customer, dto);
	        return CustomerMapper.toResponse(customer);
	    }

	    @Override
	    public void deleteCustomer(Long id,Authentication auth) {
	    	User caller = userRepo.findByUserName(auth.getName())
	                .orElseThrow(() -> new ResourceNotFoundException("Caller user not found"));

	        boolean isAdmin = caller.getRoles().stream()
	                .anyMatch(r -> "ROLE_ADMIN".equalsIgnoreCase(r.getRoleName()));

	        if (!isAdmin) {
	            Customer own = caller.getCustomer();
	            if (own == null || !own.getCustomerId().equals(id)) {
	                throw new AccessDeniedException("You can only update your own customer details");
	            }
	        }
	    	
	        Customer customer = customerRepo.findById(id)
	                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
	        customerRepo.delete(customer);
	    }

	    @Override
	    public CustomerResponseDTO addOrUpdateAddress(Long customerId, AddressRequestDTO dto,Authentication auth) {
	    	User caller = userRepo.findByUserName(auth.getName())
	                .orElseThrow(() -> new ResourceNotFoundException("Caller user not found"));

	        boolean isAdmin = caller.getRoles().stream()
	                .anyMatch(r -> "ROLE_ADMIN".equalsIgnoreCase(r.getRoleName()));

	        if (!isAdmin) {
	            Customer own = caller.getCustomer();
	            if (own == null || !own.getCustomerId().equals(customerId)) {
	                throw new AccessDeniedException("You can only update your own customer details");
	            }
	        }
	    	
	        Customer customer = customerRepo.findById(customerId)
	                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

	        Address address = customer.getAddress();
	        if (address == null) {
	            address = new Address();
	        }

	        address.setCity(dto.getCity());
	        address.setState(dto.getState());
	        address.setPincode(dto.getPincode());

	        customer.setAddress(address);
	        customerRepo.save(customer);

	        return CustomerMapper.toResponse(customer);
	    }

	
}
