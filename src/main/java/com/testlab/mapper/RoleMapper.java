package com.testlab.mapper;

import com.testlab.DTO.RoleRequestDTO;
import com.testlab.DTO.RoleResponseDTO;
import com.testlab.entities.Role;

public class RoleMapper {

    public static Role toEntity(RoleRequestDTO req) {
        if (req == null) return null;
        Role role = new Role();
        role.setRoleName(req.getRoleName());
        return role;
    }

    public static RoleResponseDTO toResponse(Role r) {
        if (r == null) return null;
        RoleResponseDTO resp = new RoleResponseDTO();
        resp.setRoleId(r.getRoleId());
        resp.setRoleName(r.getRoleName());
        return resp;
    }
}
