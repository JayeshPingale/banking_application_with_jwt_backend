package com.testlab.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testlab.DTO.AddressRequestDTO;
import com.testlab.DTO.CustomerRequestDTO;
import com.testlab.DTO.CustomerResponseDTO;
import com.testlab.DTO.CustomerUpdateDTO;
import com.testlab.Exception.NotFoundException;
import com.testlab.Repository.CustomerRepository;
import com.testlab.entities.Customer;
import com.testlab.services.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerController {
	@Autowired
	private final CustomerRepository customerRepo;

	private final CustomerService customerService;

	@PostMapping("/customers")
	public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO dto) {
		CustomerResponseDTO resp = customerService.createCustomer(dto);
		return ResponseEntity.created(URI.create("/api/customers/" + resp.getCustomerId())).body(resp);
	}

	@GetMapping("/customers/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_CUSTOMER') and #id == authentication.principal.id)")
	public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id,Authentication auth) {
		return ResponseEntity.ok(customerService.getCustomerById(id,auth));
	}

	@GetMapping("/customers")
	public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers(Authentication auth) {
		return ResponseEntity.ok(customerService.getAllCustomers(auth));
	}

	@PutMapping("/customers/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_CUSTOMER') and #id == authentication.principal.id)")
	public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Long id,
			@Valid @RequestBody CustomerUpdateDTO dto,Authentication auth) {
		return ResponseEntity.ok(customerService.updateCustomer(id, dto,auth));
	}

	@PostMapping("/customers/{id}/address")
	public ResponseEntity<Object> addOrUpdateAddress(@PathVariable Long id, @Valid @RequestBody AddressRequestDTO dto,Authentication auth) {
		return ResponseEntity.ok(customerService.addOrUpdateAddress(id, dto,auth));
	}

	@DeleteMapping("/customers/{id}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable Long id,Authentication auth) {
		customerService.deleteCustomer(id,auth);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/customers/{id}/email")
	public ResponseEntity<String> updateEmail(@PathVariable Long id, @Valid @RequestBody CustomerUpdateDTO dto) {
		Customer customer = customerRepo.findById(id).orElseThrow(() -> new NotFoundException("Customer not found"));

		customer.setEmailid(dto.getEmail());
		// No need to touch other fields
		customerRepo.save(customer);

		return ResponseEntity.ok("Email updated successfully");
	}

}