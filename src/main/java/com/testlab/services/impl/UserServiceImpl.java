package com.testlab.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.testlab.DTO.AccountResponseDTO;
import com.testlab.DTO.CustomerResponseDTO;
import com.testlab.DTO.UserRequestDTO;
import com.testlab.DTO.UserResponseDTO;
import com.testlab.DTO.UserUpdateDTO;
import com.testlab.Exception.NotFoundException;
import com.testlab.Exception.ResourceNotFoundException;
import com.testlab.Repository.RoleRepository;
import com.testlab.Repository.UserRepository;
import com.testlab.entities.Address;
import com.testlab.entities.Customer;
import com.testlab.entities.Role;
import com.testlab.entities.User;
import com.testlab.mapper.AddressMapper;
import com.testlab.mapper.UserMapper;
import com.testlab.services.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	  @Autowired
	    private JavaMailSender mailSender;
	  @Autowired
	    private PasswordEncoder passwordEncoder; 
//	@Override
//	public UserResponseDTO create(UserRequestDTO reqDTO) {
//		User user = UserMapper.toEntity(reqDTO);
//
//		// ✅ Role mapping
//		Role role = roleRepo.findByRoleName(reqDTO.getRoleName())
//				.orElseThrow(() -> new RuntimeException("Role not found"));
//		user.getRoles().add(role); // add role to user
//
//		User savedUser = userRepo.save(user);
//
//		return UserMapper.toResponse(savedUser);
//	}
	  public UserResponseDTO create(UserRequestDTO dto) {
		    User user = new User();
		    user.setUserName(dto.getUserName());
		    user.setPassword(passwordEncoder.encode(dto.getPassword()));

		    // Role set karo
		    Role role = roleRepo.findByRoleName(dto.getRoleName())
		            .orElseThrow(() -> new RuntimeException("Role not found"));
		    user.setRoles(Collections.singletonList(role));

		    // Customer set karo
		    Customer customer = new Customer();
		    customer.setEmailid(dto.getCustomer().getEmail());
		    customer.setContactNumber(dto.getCustomer().getContactNumber());
		    customer.setDob(dto.getCustomer().getDob());

		    // Address set karo
		    Address address = new Address();
		    address.setCity(dto.getAddress().getCity());
		    address.setState(dto.getAddress().getState());
		    address.setPincode(dto.getAddress().getPincode());

		    customer.setAddress(address);
		    user.setCustomer(customer);

		    // Save
		    User saved = userRepo.save(user);

		    return UserMapper.toResponse(saved);
		}

	@Override
	@Transactional(readOnly = true)
	public UserResponseDTO getUserByID(Long id, Authentication auth) {
		User usr = userRepo.findByUserName(auth.getName())
				.orElseThrow(() -> new ResourceNotFoundException("user not found"));

		boolean isAdmin = usr.getRoles().stream().anyMatch(r -> "ROLE_ADMIN".equalsIgnoreCase(r.getRoleName()));

		if (!isAdmin) {
			if (!usr.getUserId().equals(id)) {
				throw new AccessDeniedException("You do not have permission to view this user");
			}
		}
		User user = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found: " + id));
		return UserMapper.toResponse(user);
	}

	@Override
	public List<UserResponseDTO> getAllUsers(Authentication auth) {
		User usr = userRepo.findByUserName(auth.getName())
				.orElseThrow(() -> new ResourceNotFoundException("user not found"));

		boolean isAdmin = usr.getRoles().stream().anyMatch(r -> "ROLE_ADMIN".equalsIgnoreCase(r.getRoleName()));

		if (!isAdmin) {

			throw new AccessDeniedException("You do not have permission to view this user");

		}
		List<User> users = userRepo.findAll();

		return users.stream().map(user -> {
			UserResponseDTO uDto = new UserResponseDTO();
		//	uDto.setId(user.getUserId());
			uDto.setUserName(user.getUserName());

			//  Map roles (List<Role> → List<String>)
			uDto.setRoles(user.getRoles() != null
					? user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList())
					: new ArrayList<>());

			//  Map customer
			Customer c = user.getCustomer();
			if (c != null) {
				CustomerResponseDTO cDto = new CustomerResponseDTO();
				cDto.setCustomerId(c.getCustomerId());
				cDto.setEmailid(c.getEmailid());
				cDto.setContactNo(c.getContactNumber());
				cDto.setDob(c.getDob());
				cDto.setAddress(c.getAddress() != null ? AddressMapper.toResponse(c.getAddress()) : null);

				// Map accounts
				List<AccountResponseDTO> accountDtos = c.getAccounts() != null ? c.getAccounts().stream().map(a -> {
					AccountResponseDTO aDto = new AccountResponseDTO();
					aDto.setAccountId(a.getAccountId());
					aDto.setAccountNumber(a.getAccountNumber());
					aDto.setAccountType(a.getAccountType());
					aDto.setBalance(a.getBalance());
					aDto.setCustomerId(a.getCustomer() != null ? a.getCustomer().getCustomerId() : null);
					return aDto;
				}).collect(Collectors.toList()) : new ArrayList<>();
				cDto.setAccounts(accountDtos);

				uDto.setCustomer(cDto);
			}

			return uDto;
		}).collect(Collectors.toList());
	}

	@Override
	public UserResponseDTO updateUser(Long id, UserUpdateDTO req, Authentication auth) {
		User usr = userRepo.findByUserName(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        boolean isAdmin = usr.getRoles().stream()
                .anyMatch(r -> "ROLE_ADMIN".equalsIgnoreCase(r.getRoleName()));

        if (!isAdmin) {
            if (!usr.getUserId().equals(id)) {
                throw new AccessDeniedException("You do not have permission to view this user");
            }
        }
		
		User user = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found: " + id));

		// ✅ sirf password update allowed hai
		if (req.getUserName() != null || req.getRoleId() != null) {
			throw new IllegalArgumentException("Only password can be updated");
		}

		if (req.getPassword() != null) {
			user.setPassword(req.getPassword());
		}

		return UserMapper.toResponse(user);
	}

//	@Override
//	public void deleteUserbyID(Long id, Authentication auth) {
//		User usr = userRepo.findByUserName(auth.getName())
//                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
//
//        boolean isAdmin = usr.getRoles().stream()
//                .anyMatch(r -> "ROLE_ADMIN".equalsIgnoreCase(r.getRoleName()));
//
//        if (!isAdmin) {
//            if (!usr.getUserId().equals(id)) {
//                throw new AccessDeniedException("You do not have permission to view this user");
//            }
//        }
//		if (!userRepo.existsById(id)) {
//			throw new NotFoundException("User id doesn't exist: " + id);
//		}
//		userRepo.deleteById(id);
//		
//		  // Send deletion email
//	    sendAccountDeletionEmailHTML(usr);
//	}

	@Override
	public void deleteUserbyID(Long id, Authentication auth) {
	    // Get the currently logged-in user
	    User currentUser = userRepo.findByUserName(auth.getName())
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

	    boolean isAdmin = currentUser.getRoles().stream()
	            .anyMatch(r -> "ROLE_ADMIN".equalsIgnoreCase(r.getRoleName()));

	    // Only admin can delete other users
	    if (!isAdmin && !currentUser.getUserId().equals(id)) {
	        throw new AccessDeniedException("You do not have permission to delete this user");
	    }

	    // Fetch the user to delete
	    User userToDelete = userRepo.findById(id)
	            .orElseThrow(() -> new NotFoundException("User id doesn't exist: " + id));

	    // Delete the user
	    userRepo.deleteById(id);

	    // Send deletion email
	    sendAccountDeletionEmailHTML(userToDelete);
	}

	@Override
	public UserResponseDTO updatePassword(Long userId, String newPassword, Authentication auth) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new NotFoundException("User id doesn't exist: " + userId));

		user.setPassword(newPassword);
		userRepo.save(user);

		return UserMapper.toResponse(user);
	}
	
	public void sendAccountDeletionEmailHTML(User user) {
	    if (user.getCustomer() != null && user.getCustomer().getEmailid() != null) {
	        String toEmail = user.getCustomer().getEmailid();
	        String subject = "⚠️ Account Deletion Confirmation - Lena Bank";

	        StringBuilder body = new StringBuilder();
	        body.append("<html><body>");
	        body.append("<h2 style='color: #d9534f;'>Account Deleted Successfully</h2>");
	        body.append("<p>Dear <b>").append(user.getUserName()).append("</b>,</p>");
	        body.append("<p>Your account with <b>Lena Bank</b> has been <b>successfully deleted</b>.</p>");
	        
//	        body.append("<h4>Account Details:</h4>");
//	        body.append("<table border='1' cellpadding='5' cellspacing='0' style='border-collapse: collapse;'>")
//	            .append("<tr><th>User Name</th><td>").append(user.getUserName()).append("</td></tr>")
//	            .append("<tr><th>Customer ID</th><td>").append(user.get).append("</td></tr>")
//	            .append("</table>");
//	        
	        // Include account numbers
	        if (user.getCustomer().getAccounts() != null && !user.getCustomer().getAccounts().isEmpty()) {
	            body.append("<p>Deleted Account Number(s):</p>");
	            body.append("<ul>");
	            user.getCustomer().getAccounts().forEach(acc -> {
	                body.append("<li>").append(acc.getAccountNumber()).append("</li>");
	            });
	            body.append("</ul>");
	        }
	        body.append("<p style='color: #d9534f;'>If you did not request this deletion or believe this was a mistake, please contact us immediately.</p>");
	        body.append("<p>Thank you for banking with us.<br/><b>The Lena Bank Team</b></p>");
	        body.append("</body></html>");

	        try {
	            MimeMessage message = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);
	            helper.setTo(toEmail);
	            helper.setSubject(subject);
	            helper.setText(body.toString(), true); // true = HTML
	            mailSender.send(message);
	        } catch (MessagingException e) {
	            System.err.println("Failed to send account deletion email: " + e.getMessage());
	        }
	    }
	}

}
