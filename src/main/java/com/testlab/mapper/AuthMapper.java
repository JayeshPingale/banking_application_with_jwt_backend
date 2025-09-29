package com.testlab.mapper;

import org.springframework.stereotype.Component;

import com.testlab.DTO.AddressRequestDTO;
import com.testlab.DTO.security.RegistrationDto;
import com.testlab.entities.Address;
import com.testlab.entities.Customer;
import com.testlab.entities.User;

@Component
public class AuthMapper {

    // Map AddressRequestDTO → Address entity
    private Address toAddressEntity(AddressRequestDTO dto) {
        if (dto == null) return null;

        Address address = new Address();
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPincode(dto.getPincode());
        return address;
    }

    // Map RegistrationDto → User entity
    public User toUserEntity(RegistrationDto dto) {
        User user = new User();
        user.setUserName(dto.getUsername());
        user.setPassword(dto.getPassword()); // encode in service later

        // Map Customer
        if (dto.getCustomer() != null) {
            Customer customer = new Customer();
            customer.setEmailid(dto.getCustomer().getEmail());
            customer.setContactNumber(dto.getCustomer().getContactNumber());
            customer.setDob(dto.getCustomer().getDob());

            // Map Address inside Customer
            customer.setAddress(toAddressEntity(dto.getAddress()));
            user.setCustomer(customer);
        }

        return user;
    }
}
