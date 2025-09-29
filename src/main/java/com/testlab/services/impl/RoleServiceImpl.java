package com.testlab.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.testlab.DTO.RoleRequestDTO;
import com.testlab.DTO.RoleResponseDTO;
import com.testlab.Repository.RoleRepository;
import com.testlab.Repository.UserRepository;
import com.testlab.entities.Role;
import com.testlab.entities.User;
import com.testlab.services.RoleService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;

    @Override
    public RoleResponseDTO createRole(RoleRequestDTO dto) {
        // Only allow ROLE_ADMIN or ROLE_CUSTOMER
        String roleName = dto.getRoleName().toUpperCase();
        if (!roleName.equals("ROLE_ADMIN") && !roleName.equals("ROLE_CUSTOMER")) {
            throw new IllegalArgumentException("Role must be either ROLE_ADMIN or ROLE_CUSTOMER");
        }

        Role role = new Role();
        role.setRoleName(roleName);
        roleRepo.save(role);

        RoleResponseDTO response = new RoleResponseDTO();
        response.setRoleId(role.getRoleId());
        response.setRoleName(role.getRoleName());
        return response;
    }

    @Override
    public List<RoleResponseDTO> getAllRoles() {
        return roleRepo.findAll().stream().map(role -> {
            RoleResponseDTO dto = new RoleResponseDTO();
            dto.setRoleId(role.getRoleId());
            dto.setRoleName(role.getRoleName());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<RoleResponseDTO> getRolesByUserId(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return user.getRoles().stream().map(role -> {
            RoleResponseDTO dto = new RoleResponseDTO();
            dto.setRoleId(role.getRoleId());
            dto.setRoleName(role.getRoleName());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteRole(Long id) {
        Role role = roleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        roleRepo.delete(role);
    }
}

