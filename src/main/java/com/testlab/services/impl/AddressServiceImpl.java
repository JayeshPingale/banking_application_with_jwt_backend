package com.testlab.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.testlab.DTO.AddressRequestDTO;
import com.testlab.DTO.AddressResponseDTO;
import com.testlab.Exception.NotFoundException;
import com.testlab.Repository.AddressRepository;
import com.testlab.Repository.CustomerRepository;
import com.testlab.entities.Address;
import com.testlab.entities.Customer;
import com.testlab.mapper.AddressMapper;
import com.testlab.services.AddressService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
	@Autowired
	private final AddressRepository addressRepo;

	private final CustomerRepository custRepo;

	@Override
	public AddressResponseDTO createAddress(AddressRequestDTO dto) {
		Address address = AddressMapper.toEntity(dto);
		return AddressMapper.toResponse(addressRepo.save(address));
	}

	@Override
	@Transactional(readOnly = true)
	public AddressResponseDTO getAddressById(Long id) {
		Address address = addressRepo.findById(id).orElseThrow(() -> new NotFoundException("Address not found: " + id));
		return AddressMapper.toResponse(address);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AddressResponseDTO> getAllAddresses() {
		return addressRepo.findAll().stream().map(AddressMapper::toResponse).toList();
	}

//	@Override
//	public AddressResponseDTO updateAddress(Long id, AddressRequestDTO dto) {
//		Address existing = addressRepo.findById(id)
//				.orElseThrow(() -> new NotFoundException("Address not found: " + id));
//
//		// fields update
//		existing.setCity(dto.getCity());
//		existing.setState(dto.getState());
//		existing.setPincode(dto.getPincode());
//
//		return AddressMapper.toResponse(addressRepo.save(existing));
//	}

	@Override
	public void deleteAddress(Long id) {
		if (!addressRepo.existsById(id)) {
			throw new NotFoundException("Address not found: " + id);
		}
		addressRepo.deleteById(id);
	}

	@Override
	@Transactional
	public AddressResponseDTO updateAddressByCustomerId(Long customerId, AddressRequestDTO dto) {
		Customer customer = custRepo.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));

		Address address = customer.getAddress();
		if (address == null) {
			throw new RuntimeException("No address found for this customer");
		}

		// Update fields
		address.setCity(dto.getCity());
		address.setState(dto.getState());
		address.setPincode(dto.getPincode());

		// Persist changes
		customer.setAddress(address);
		custRepo.save(customer);

		return AddressMapper.toResponse(address);
	}
}
