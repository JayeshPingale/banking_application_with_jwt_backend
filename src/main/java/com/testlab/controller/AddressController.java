package com.testlab.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testlab.DTO.AddressRequestDTO;
import com.testlab.DTO.AddressResponseDTO;
import com.testlab.Exception.NotFoundException;
import com.testlab.Repository.CustomerRepository;
import com.testlab.entities.Address;
import com.testlab.entities.Customer;
import com.testlab.services.AddressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
   @Autowired
    private CustomerRepository customerRepo;

    @PostMapping
    public ResponseEntity<AddressResponseDTO> createAddress(@Valid @RequestBody AddressRequestDTO dto) {
        AddressResponseDTO resp = addressService.createAddress(dto);
        return ResponseEntity.created(URI.create("/api/addresses/" + resp.getAddressId())).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> getAddressById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getAddressById(id));
    }

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getAllAddresses() {
        return ResponseEntity.ok(addressService.getAllAddresses());
    }
    
    
    // Update address by customerId
    @PutMapping("/{customerId}/address")
    public ResponseEntity<AddressResponseDTO> updateAddress(
            @PathVariable Long customerId,
            @Valid @RequestBody AddressRequestDTO dto) {

        AddressResponseDTO response = addressService.updateAddressByCustomerId(customerId, dto);
        return ResponseEntity.ok(response);
    }
    @PostMapping("customers/{customerId}")
    public ResponseEntity<String> addOrUpdateAddress(
            @PathVariable Long customerId,
            @RequestBody AddressRequestDTO addressDto) {

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        Address address = customer.getAddress() != null ? customer.getAddress() : new Address();
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setPincode(addressDto.getPincode());

        customer.setAddress(address);
        customerRepo.save(customer);

        return ResponseEntity.ok("Address added/updated successfully");
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok("Address deleted successfully");
    }
}
