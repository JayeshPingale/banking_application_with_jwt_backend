package com.testlab.mapper;

import java.util.stream.Collectors;

import com.testlab.DTO.AddressResponseDTO;
import com.testlab.DTO.CustomerResponseDTO;
import com.testlab.DTO.CustomerUpdateDTO;
import com.testlab.DTO.UserRequestDTO;
import com.testlab.DTO.UserResponseDTO;
import com.testlab.DTO.UserUpdateDTO;
import com.testlab.Repository.RoleRepository;
import com.testlab.entities.Address;
import com.testlab.entities.Customer;
import com.testlab.entities.Role;
import com.testlab.entities.User;

public class UserMapper {

	static RoleRepository roleRepo;
    private UserMapper() {}

    // RequestDTO -> Entity
    public static User toEntity(UserRequestDTO req) {
        if (req == null) return null;

        User user = new User();
        user.setUserName(req.getUserName());
        user.setPassword(req.getPassword());

        // Customer mapping
        if (req.getCustomer() != null) {
            Customer customer = new Customer();
            customer.setEmailid(req.getCustomer().getEmail());
            customer.setContactNumber(req.getCustomer().getContactNumber());
            customer.setDob(req.getCustomer().getDob());

            // Address mapping via customer
            if (req.getAddress() != null) {
                Address address = new Address();
                address.setCity(req.getAddress().getCity());
                address.setState(req.getAddress().getState());
                address.setPincode(req.getAddress().getPincode());
                customer.setAddress(address);
            }

            user.setCustomer(customer);
        }

        return user;
    }

    // Entity -> ResponseDTO
//    public static UserResponseDTO toResponse(User e) {
//        if (e == null) return null;
//
//        UserResponseDTO resp = new UserResponseDTO();
//        resp.setId(e.getUserId());
//        resp.setUserName(e.getUserName());
//
//        // Role mapping (first role for simplicity)
//        if (e.getRoles() != null && !e.getRoles().isEmpty()) {
//            resp.setRoles(
//                e.getRoles().stream()
//                        .map(Role::getRoleName)   // entity se sirf roleName nikaalo
//                        .collect(Collectors.toList())   // list banao
//            );
//        }
//
//        // Customer mapping
//        if (e.getCustomer() != null) {
//            CustomerResponseDTO custResp = new CustomerResponseDTO();
//            custResp.setCustomerId(e.getCustomer().getCustomerId());
//            custResp.setEmailid(e.getCustomer().getEmailid());
//            custResp.setContactNo(e.getCustomer().getContactNumber());
//            custResp.setDob(e.getCustomer().getDob());
//          
//
//            // Address mapping
//            if (e.getCustomer().getAddress() != null) {
//                AddressResponseDTO addrResp = new AddressResponseDTO();
//                addrResp.setAddressId(e.getCustomer().getAddress().getAddressId());
//                addrResp.setCity(e.getCustomer().getAddress().getCity());
//                addrResp.setState(e.getCustomer().getAddress().getState());
//                addrResp.setPincode(e.getCustomer().getAddress().getPincode());
//                custResp.setAddress(addrResp);
//            }
//            resp.setCustomer(custResp);
//        }
//
//        return resp;
//    }
    
    public static UserResponseDTO toResponse(User e) {
        if (e == null) return null;

        UserResponseDTO resp = new UserResponseDTO();
        resp.setUserName(e.getUserName());

        // Role mapping
        if (e.getRoles() != null && !e.getRoles().isEmpty()) {
            resp.setRoles(
                e.getRoles().stream()
                        .map(Role::getRoleName)
                        .collect(Collectors.toList())
            );
        }

        // Customer mapping
        if (e.getCustomer() != null) {
            CustomerResponseDTO custResp = new CustomerResponseDTO();
            custResp.setEmailid(e.getCustomer().getEmailid());
            custResp.setContactNo(e.getCustomer().getContactNumber());
            custResp.setDob(e.getCustomer().getDob());

            // Address mapping
            if (e.getCustomer().getAddress() != null) {
                AddressResponseDTO addrResp = new AddressResponseDTO();
                addrResp.setCity(e.getCustomer().getAddress().getCity());
                addrResp.setState(e.getCustomer().getAddress().getState());
                addrResp.setPincode(e.getCustomer().getAddress().getPincode());
                custResp.setAddress(addrResp);
            }

            resp.setCustomer(custResp);
        }

        return resp;
    }

 // Entity -> ResponseDTO
//    public static UserResponseDTO toResponse(User e) {
//        if (e == null) return null;
//
//        UserResponseDTO resp = new UserResponseDTO();   // 👈 resp object banaya
//        resp.setUserName(e.getUserName());              // 👈 sirf userName set karo
//
//        // Role mapping
//        if (e.getRoles() != null && !e.getRoles().isEmpty()) {
//            resp.setRoles(
//                e.getRoles().stream()
//                        .map(Role::getRoleName)   // sirf roleName
//                        .collect(Collectors.toList())
//            );
//        }
//
//        return resp;
//    }
    // Update entity
    public static void updateEntity(User user, UserUpdateDTO req) {
        if (user == null || req == null) return;

        if (req.getUserName() != null) user.setUserName(req.getUserName());
        if (req.getPassword() != null) user.setPassword(req.getPassword());
        // Role update service layer me handle hoga
    }
    
    public static void updateUserFromDTO(User user, UserUpdateDTO dto) {
        if (dto.getUserName() != null) user.setUserName(dto.getUserName());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword());

        if (dto.getRoleId() != null) {
            Role role = roleRepo.findById(dto.getRoleId()).orElseThrow(() -> new RuntimeException("Role not found"));
            user.getRoles().clear();
            user.getRoles().add(role);
        }

        if (dto.getCustomer() != null) {
            Customer c = user.getCustomer();
            if (c == null) {
                c = new Customer();
                user.setCustomer(c);
            }
            CustomerUpdateDTO cDto = dto.getCustomer();
            if (cDto.getEmail() != null) c.setEmailid(cDto.getEmail());
            if (cDto.getContactNo() != null) c.setContactNumber(cDto.getContactNo());
            

//            if (cDto.getAddress() != null) {
//                Address a = c.getAddress();
//                if (a == null) {
//                    a = new Address();
//                    c.setAddress(a);
//                }
//                AddressUpdateDTO aDto = cDto.getAddress();
//                if (aDto.getCity() != null) a.setCity(aDto.getCity());
//                if (aDto.getState() != null) a.setState(aDto.getState());
//                if (aDto.getPincode() != null) a.setPincode(aDto.getPincode());
//            }
        }
    }

}
