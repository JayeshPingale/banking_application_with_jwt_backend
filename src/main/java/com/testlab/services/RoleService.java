package com.testlab.services;

import java.util.List;

import com.testlab.DTO.RoleRequestDTO;
import com.testlab.DTO.RoleResponseDTO;

public interface RoleService {
    RoleResponseDTO createRole(RoleRequestDTO dto);

    List<RoleResponseDTO> getAllRoles();

    List<RoleResponseDTO> getRolesByUserId(Long userId);

    void deleteRole(Long id);
}
