package com.testlab.services;

import java.util.List;

import com.testlab.DTO.AddressRequestDTO;
import com.testlab.DTO.AddressResponseDTO;

public interface AddressService {
	   AddressResponseDTO createAddress(AddressRequestDTO dto);

	    AddressResponseDTO getAddressById(Long id);

	    List<AddressResponseDTO> getAllAddresses();

//	    AddressResponseDTO updateAddress(Long id, AddressRequestDTO dto);

	    void deleteAddress(Long id);
	    
	    AddressResponseDTO updateAddressByCustomerId(Long customerId, AddressRequestDTO dto);
}
