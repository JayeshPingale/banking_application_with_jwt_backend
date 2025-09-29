package com.testlab.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.testlab.DTO.AccountResponseDTO;
import com.testlab.DTO.AddressResponseDTO;
import com.testlab.DTO.CustomerRequestDTO;
import com.testlab.DTO.CustomerResponseDTO;
import com.testlab.DTO.CustomerUpdateDTO;
import com.testlab.entities.Address;
import com.testlab.entities.Customer;
@Component
public class CustomerMapper {
	public static Customer toEntity(CustomerRequestDTO dto) {
	    Customer customer = new Customer();
	    customer.setEmailid(dto.getEmail());
	    customer.setContactNumber(dto.getContactNumber());
	    customer.setDob(dto.getDob());

	    if (dto.getAddress() != null) {
	        Address address = new Address();
	        address.setCity(dto.getAddress().getCity());
	        address.setState(dto.getAddress().getState());
	        address.setPincode(dto.getAddress().getPincode());

	        // maintain bi-directional relation
	        address.setCustomer(customer);
	        customer.setAddress(address);
	    }

	    return customer;
	}


//    public static Customer toEntity(CustomerRequestDTO req) {
//        if (req == null) return null;
//        Customer cust = new Customer();
//        cust.setEmailid(req.getEmail());
//        cust.setContactNumber(req.getContactNumber());
//        cust.setDob(req.getDob());
//        cust.setAddress(AddressMapper.toEntity(req.getAddress()));
//        return cust;
//    }
//
//    public static CustomerResponseDTO toResponse(Customer c) {
//        if (c == null) return null;
//        CustomerResponseDTO resp = new CustomerResponseDTO();
//        resp.setEmailid(c.getEmailid());
//        resp.setContactNo(c.getContactNumber());
//        resp.setDob(c.getDob());
//        return resp;
//    }
//    public static CustomerResponseDTO toResponse(Customer customer) {
//        CustomerResponseDTO dto = new CustomerResponseDTO();
//        dto.setCustomerId(customer.getCustomerId());
//        dto.setEmailid(customer.getEmailid());
//        dto.setContactNo(customer.getContactNumber());
//        dto.setDob(customer.getDob());
//
//        if (customer.getAddress() != null) {
//            AddressResponseDTO addr = new AddressResponseDTO();
//            addr.setAddressId(customer.getAddress().getAddressId());
//            addr.setCity(customer.getAddress().getCity());
//            addr.setState(customer.getAddress().getState());
//            addr.setPincode(customer.getAddress().getPincode());
//            dto.setAddress(addr);
//        }
//
//        return dto;
//    }
    public static CustomerResponseDTO toResponse(Customer customer) {
        if (customer == null) return null;

        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setCustomerId(customer.getCustomerId());
        dto.setEmailid(customer.getEmailid());
        dto.setContactNo(customer.getContactNumber());
        dto.setDob(customer.getDob());

        // Address mapping
        if (customer.getAddress() != null) {
            AddressResponseDTO addressDTO = new AddressResponseDTO();
            addressDTO.setAddressId(customer.getAddress().getAddressId());
            addressDTO.setCity(customer.getAddress().getCity());
            addressDTO.setState(customer.getAddress().getState());
            addressDTO.setPincode(customer.getAddress().getPincode());
            dto.setAddress(addressDTO);
        }

//        // Accounts mapping
//        if (customer.getAccounts() != null && !customer.getAccounts().isEmpty()) {
//            dto.setAccounts(customer.getAccounts().stream()
//                .map(account -> {
//                    AccountResponseDTO accDto = new AccountResponseDTO();
//                    accDto.setAccountId(account.getAccountId());
//                    accDto.setAccountNumber(account.getAccountNumber());
//                    accDto.setAccountType(account.getAccountType());
//                    accDto.setBalance(account.getBalance());
//                    return accDto;
//                })
//                .collect(Collectors.toList()));
// }
     // Accounts mapping
        if (customer.getAccounts() != null && !customer.getAccounts().isEmpty()) {
            dto.setAccounts(customer.getAccounts().stream()
                .map(account -> {
                    AccountResponseDTO accDto = new AccountResponseDTO();
                    accDto.setAccountId(account.getAccountId());
                    accDto.setAccountNumber(account.getAccountNumber());
                    accDto.setAccountType(account.getAccountType());
                    accDto.setBalance(account.getBalance());
                    accDto.setCustomerId(account.getCustomer() != null ? account.getCustomer().getCustomerId() : null); 
                    return accDto;
                })
                .collect(Collectors.toList()));
        }

        return dto;
    }
    public static void updateEntity(Customer customer, CustomerUpdateDTO dto) {
        if (customer == null || dto == null) return;

        // Update email and contact number
        if (dto.getEmail() != null) {
            customer.setEmailid(dto.getEmail());
        }
        if (dto.getContactNo() != null) {
            customer.setContactNumber(dto.getContactNo());
        }

        // DOB is intentionally NOT updated

        // Update address
        Address address = customer.getAddress();
        if (address == null) {
            address = new Address();
            customer.setAddress(address);
        }

        if (dto.getCity() != null) {
            address.setCity(dto.getCity());
        }
        if (dto.getState() != null) {
            address.setState(dto.getState());
        }
        if (dto.getPincode() != null) {
            address.setPincode(dto.getPincode());
        }

    }

    
    }
