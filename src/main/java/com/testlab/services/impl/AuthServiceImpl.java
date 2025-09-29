package com.testlab.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.testlab.DTO.AccountResponseDTO;
import com.testlab.DTO.AddressResponseDTO;
import com.testlab.DTO.CustomerResponseDTO;
import com.testlab.DTO.UserResponseDTO;
import com.testlab.DTO.security.LoginDto;
import com.testlab.DTO.security.RegistrationDto;
import com.testlab.DTO.security.security.JwtTokenProvider;
import com.testlab.Exception.UserApiException;
import com.testlab.Repository.RoleRepository;
import com.testlab.Repository.UserRepository;
import com.testlab.entities.Customer;
import com.testlab.entities.Role;
import com.testlab.entities.User;
import com.testlab.mapper.AuthMapper;
import com.testlab.mapper.CustomerMapper;
import com.testlab.services.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	   @Autowired
	    private JavaMailSender mailSender;


//	@Autowired
//	private MailConfig emailService;
//    @Override
//    public UserResponseDTO register(RegistrationDto registerDto) {
//
//        // Check if user already exists
//        if (userRepo.existsByUserName(registerDto.getUsername())) {
//            throw new UserApiException(HttpStatus.BAD_REQUEST, "User already exists");
//        }
//
//        User user = new User();
//        user.setUserName(registerDto.getUsername());
//        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
//
//        // Fetch role from DB
//        Role userRole = roleRepo.findByRoleName(registerDto.getRole())
//                .orElseThrow(() -> new UserApiException(HttpStatus.BAD_REQUEST, "Role does not exist"));
//
////        // Assign role to user (since user has List<Role>)
////        List<Role> roles = new ArrayList<>();
////        roles.add(userRole);
////        user.setRoles(roles);
//        user.getRoles().add(userRole); 
//
//        // Save user
//        user = userRepo.save(user);
//
//        // Prepare response
//        UserResponseDTO dto = new UserResponseDTO();
//        dto.setId(user.getUserId());
//        dto.setUserName(user.getUserName());
//        dto.setRoles(
//        	    user.getRoles()
//        	        .stream()
//        	        .map(Role::getRoleName)   // sirf role ka naam
//        	        .collect(Collectors.toList())
//        	); // ✅ ab response me roles dikhega
//        dto.setCustomer(user.getCustomer());
//    
//        return dto;
//    }
////    dto.setRoles(
//    user.getRoles()
//    .stream()
//    .map(Role::getRoleName)   // sirf role ka naam
//    .collect(Collectors.toList()
	@Autowired
	private AuthMapper authMapper;
//    @Override
//    public UserResponseDTO register(RegistrationDto registerDto) {
//
//        // 1️⃣ Check if user already exists
//        if (userRepo.existsByUserName(registerDto.getUsername())) {
//            throw new UserApiException(HttpStatus.BAD_REQUEST, "User already exists");
//        }
//
//        // 2️⃣ Create User entity
//        User user = new User();
//        user.setUserName(registerDto.getUsername());
//        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
//
//        // 3️⃣ Fetch Role from DB
//        Role userRole = roleRepo.findByRoleName(registerDto.getRole())
//                .orElseThrow(() -> new UserApiException(HttpStatus.BAD_REQUEST, "Role does not exist"));
//
//        // 4️⃣ Assign role to user
//        user.getRoles().add(userRole);
//
//        // 5️⃣ Set Customer info if provided
//        if (registerDto.getCustomer() != null) {
//            Customer customer = new Customer();
//            customer.setEmailid(registerDto.getCustomer().getEmailid());
//            customer.setContactNumber(registerDto.getCustomer().getContactNo());
//            customer.setDob(registerDto.getCustomer().getDob());
//
//            if (registerDto.getAddress() != null) {
//                Address address = new Address();
//                address.setCity(registerDto.getAddress().getCity());
//                address.setState(registerDto.getAddress().getState());
//                address.setPincode(registerDto.getAddress().getPincode());
//                customer.setAddress(address);
//            }
//
//            user.setCustomer(customer);
//        }
//
//        // 6️⃣ Save User (cascade saves customer & address)
//        user = userRepo.save(user);
//
//        // 7️⃣ Prepare Response DTO
//        UserResponseDTO dto = new UserResponseDTO();
//        dto.setId(user.getUserId());
//        dto.setUserName(user.getUserName());
//
//        // Roles as List<String>
//        dto.setRoles(
//            user.getRoles().stream()
//                .map(Role::getRoleName)
//                .collect(Collectors.toList())
//        );
//
//        // Map CustomerResponseDTO
//        if (user.getCustomer() != null) {
//            CustomerResponseDTO customerDTO = new CustomerResponseDTO();
//            customerDTO.setEmailid(user.getCustomer().getEmailid());
//            customerDTO.setContactNo(user.getCustomer().getContactNumber());
//            customerDTO.setDob(user.getCustomer().getDob());
//
//            if (user.getCustomer().getAddress() != null) {
//                AddressResponseDTO addressDTO = new AddressResponseDTO();
//                addressDTO.setCity(user.getCustomer().getAddress().getCity());
//                addressDTO.setState(user.getCustomer().getAddress().getState());
//                addressDTO.setPincode(user.getCustomer().getAddress().getPincode());
//                customerDTO.setAddress(addressDTO);
//            }
//
//            dto.setCustomer(customerDTO);
//        }
//
//        return dto;
//    }
//    @Override
//    public UserResponseDTO register(RegistrationDto registerDto) {
//
//        // 1️⃣ Check if user already exists
//        if (userRepo.existsByUserName(registerDto.getUsername())) {
//            throw new UserApiException(HttpStatus.BAD_REQUEST, "User already exists");
//        }
//
//        // 2️⃣ Create User entity
//        User user = new User();
//        user.setUserName(registerDto.getUsername());
//        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
//
//        // 3️⃣ Fetch Role from DB
//        Role userRole = roleRepo.findByRoleName(registerDto.getRole())
//                .orElseThrow(() -> new UserApiException(HttpStatus.BAD_REQUEST, "Role does not exist"));
//
//        // 4️⃣ Assign role to user
//        user.getRoles().add(userRole);
//
//        // 5️⃣ Set Customer info if provided
//        if (registerDto.getCustomer() != null) {
//            Customer customer = new Customer();
//            customer.setEmailid(registerDto.getCustomer().getEmail());          // ✅ matches CustomerRequestDTO
//            customer.setContactNumber(registerDto.getCustomer().getContactNumber()); // ✅ matches DTO
//            customer.setDob(registerDto.getCustomer().getDob());
//
//            if (registerDto.getCustomer().getAddress() != null) {
//                AddressRequestDTO addrDTO = registerDto.getCustomer().getAddress();
//                Address address = new Address();
//                address.setCity(addrDTO.getCity());
//                address.setState(addrDTO.getState());
//                address.setPincode(addrDTO.getPincode());
//                customer.setAddress(address);
//            }
//
//            user.setCustomer(customer); // ✅ cascade saves Customer & Address
//        }
//
//        // 6️⃣ Save User
//        user = userRepo.save(user);
//
//        // 7️⃣ Prepare Response DTO
//        UserResponseDTO dto = new UserResponseDTO();
//        dto.setId(user.getUserId());
//        dto.setUserName(user.getUserName());
//
//        // Roles as List<String>
//        dto.setRoles(
//            user.getRoles().stream()
//                .map(Role::getRoleName)
//                .collect(Collectors.toList())
//        );
//
//        // CustomerResponseDTO mapping
//        if (user.getCustomer() != null) {
//            CustomerResponseDTO customerDTO = new CustomerResponseDTO();
//            customerDTO.setEmailid(user.getCustomer().getEmailid());
//            customerDTO.setContactNo(user.getCustomer().getContactNumber());
//            customerDTO.setDob(user.getCustomer().getDob());
//
//            if (user.getCustomer().getAddress() != null) {
//                AddressResponseDTO addressDTO = new AddressResponseDTO();
//                addressDTO.setCity(user.getCustomer().getAddress().getCity());
//                addressDTO.setState(user.getCustomer().getAddress().getState());
//                addressDTO.setPincode(user.getCustomer().getAddress().getPincode());
//                customerDTO.setAddress(addressDTO);
//            }
//
//            dto.setCustomer(customerDTO);
//        }
//
//        return dto;
//    }

//	@Override
//	public UserResponseDTO register(RegistrationDto registerDto) {
//
//		// Check if user exists
//		if (userRepo.existsByUserName(registerDto.getUsername())) {
//			throw new UserApiException(HttpStatus.BAD_REQUEST, "User already exists");
//		}
//
//		// Create User entity
//		User user = new User();
//		user.setUserName(registerDto.getUsername());
//		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
//
//		// Assign Role
//		Role role = roleRepo.findByRoleName(registerDto.getRole())
//				.orElseThrow(() -> new UserApiException(HttpStatus.BAD_REQUEST, "Role does not exist"));
//		user.getRoles().add(role);
//
////		// Map Customer
//		if (registerDto.getCustomer() != null) {
//			Customer customer = CustomerMapper.toEntity(registerDto.getCustomer());
//			user.setCustomer(customer);
//		}
//
//		// Save User (cascade saves Customer, Address, Accounts)
//		user = userRepo.save(user);
//		
////		//send mail 
////		try {
////			emailService.sendAccountCreationMail(user.getCustomer().getEmailid(), user.getUserName());
////		} catch (Exception e) {
////			System.out.println("Mail bhejne me error: " + e.getMessage());
////		}
//		sendRegistrationMail(user);
//		// Prepare Response DTO
//		UserResponseDTO dto = new UserResponseDTO();
//		//dto.setId(user.getUserId());
//		dto.setUserName(user.getUserName());
//
//		// Map roles
//		dto.setRoles(user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()));
//
//		// Map CustomerResponseDTO
//		if (user.getCustomer() != null) {
//			CustomerResponseDTO custDto = new CustomerResponseDTO();
//			custDto.setCustomerId(user.getCustomer().getCustomerId());
//			custDto.setEmailid(user.getCustomer().getEmailid());
//			custDto.setContactNo(user.getCustomer().getContactNumber());
//			custDto.setDob(user.getCustomer().getDob());
//
//			// AddressResponseDTO
//			if (user.getCustomer().getAddress() != null) {
//				AddressResponseDTO addrDto = new AddressResponseDTO();
//				addrDto.setCity(user.getCustomer().getAddress().getCity());
//				addrDto.setState(user.getCustomer().getAddress().getState());
//				addrDto.setPincode(user.getCustomer().getAddress().getPincode());
//				custDto.setAddress(addrDto);
//			}
//
//			// AccountResponseDTO(s)
//			if (user.getCustomer().getAccounts() != null) {
//				List<AccountResponseDTO> accounts = user.getCustomer().getAccounts().stream().map(acc -> {
//					AccountResponseDTO accDto = new AccountResponseDTO();
//					accDto.setAccountId(acc.getAccountId());
//					accDto.setAccountNumber(acc.getAccountNumber());
//					accDto.setBalance(acc.getBalance());
//					return accDto;
//				}).collect(Collectors.toList());
//				custDto.setAccounts(accounts);
//			}
//
//			dto.setCustomer(custDto);
//		}
//
//		return dto;
//	}
	
	@Override
	public UserResponseDTO register(RegistrationDto registerDto) {
		// Check if user exists
		if (userRepo.existsByUserName(registerDto.getUsername())) {
		    throw new UserApiException(HttpStatus.BAD_REQUEST, "User already exists");
		}

		// Create User entity
		User user = new User();
		user.setUserName(registerDto.getUsername());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

		// Assign Role
		Role role = roleRepo.findByRoleName(registerDto.getRole())
		        .orElseThrow(() -> new UserApiException(HttpStatus.BAD_REQUEST, "Role does not exist"));
		user.getRoles().add(role);

		// Map Customer if provided
		if (registerDto.getCustomer() != null) {
		    Customer customer = CustomerMapper.toEntity(registerDto.getCustomer());
		    user.setCustomer(customer); // cascade should save address/accounts
		}

		// Save User
		user = userRepo.save(user);

		// Send registration mail
		sendRegistrationMail(user);

		// Prepare Response DTO
		UserResponseDTO dto = new UserResponseDTO();
		dto.setUserName(user.getUserName());

		// Map roles
		dto.setRoles(user.getRoles().stream()
		        .map(Role::getRoleName)
		        .collect(Collectors.toList()));

		// Map customer
		if (user.getCustomer() != null) {
		    CustomerResponseDTO custDto = new CustomerResponseDTO();
		    custDto.setEmailid(user.getCustomer().getEmailid());
		    custDto.setContactNo(user.getCustomer().getContactNumber());
		    custDto.setDob(user.getCustomer().getDob());

		    // Map Address
		    if (user.getCustomer().getAddress() != null) {
		        AddressResponseDTO addrDto = new AddressResponseDTO();
		        addrDto.setCity(user.getCustomer().getAddress().getCity());
		        addrDto.setState(user.getCustomer().getAddress().getState());
		        addrDto.setPincode(user.getCustomer().getAddress().getPincode());
		        custDto.setAddress(addrDto);
		    }

		    // Map Accounts
		    if (user.getCustomer().getAccounts() != null) {
		        List<AccountResponseDTO> accounts = user.getCustomer().getAccounts().stream().map(acc -> {
		            AccountResponseDTO accDto = new AccountResponseDTO();
		            accDto.setAccountNumber(acc.getAccountNumber());
		            accDto.setBalance(acc.getBalance());
		            return accDto;
		        }).collect(Collectors.toList());
		        custDto.setAccounts(accounts);
		    }

		    dto.setCustomer(custDto);
		}

		// Return response (id field is not set)
		return dto;

	}
	@Override
	public String login(LoginDto loginDto) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			return tokenProvider.generateToken(authentication);

		} catch (BadCredentialsException e) {
			throw new UserApiException(HttpStatus.NOT_FOUND, "Username or Password is incorrect");
		}
	}
//    private void sendRegistrationMail(User user) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(user.getCustomer().getEmailid());  
//        message.setSubject("Account Created Successfully");
//        message.setText("Hi " + user.getUserName() + ",\n\nYour account has been created successfully!\n\nThank you for registering with us.");
//
//        mailSender.send(message);
//    }
	private void sendRegistrationMail(User user) {
	    if (user.getCustomer() != null && user.getCustomer().getEmailid() != null) {
	        String toEmail = user.getCustomer().getEmailid();
	        String userName = user.getUserName();

	        String subject = "🎉 Welcome to Lena Bank, " + userName + "!";
	        
	        String text = "Hi " + userName + ",\n\n"
	                + "Welcome to Lena Bank! 🏦\n\n"
	                + "We are thrilled to have you onboard. Your account has been successfully created.\n\n"
	                + "Here’s a quick overview:\n"
	                + "Username: " + userName + "\n"
	                + "Email: " + toEmail + "\n\n"
	                + "Feel free to explore our services and enjoy seamless banking.\n\n"
	                + "Thank you for choosing Lena Bank!\n"
	                + "— The Lena Bank Team";

	        try {
	            SimpleMailMessage message = new SimpleMailMessage();
	            message.setTo(toEmail);
	            message.setSubject(subject);
	            message.setText(text);
	            
	            mailSender.send(message);
	        } catch (Exception e) {
	            System.err.println("Failed to send registration mail: " + e.getMessage());
	        }
	    }
	}

}
