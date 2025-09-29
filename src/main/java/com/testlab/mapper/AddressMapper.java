package com.testlab.mapper;

import com.testlab.DTO.AddressRequestDTO;
import com.testlab.DTO.AddressResponseDTO;
import com.testlab.entities.Address;

public class AddressMapper {

	public static Address toEntity(AddressRequestDTO dto) {
	    if (dto == null) return null;
	    Address addr = new Address();
	    addr.setCity(dto.getCity());
	    addr.setState(dto.getState());
	    addr.setPincode(dto.getPincode());
	    return addr;
	}

    public static AddressResponseDTO toResponse(Address addr) {
        if(addr == null) return null;
        AddressResponseDTO dto = new AddressResponseDTO();
        dto.setAddressId(addr.getAddressId());
        dto.setCity(addr.getCity());
        dto.setState(addr.getState());
        dto.setPincode(addr.getPincode());
        return dto;
    }
}
