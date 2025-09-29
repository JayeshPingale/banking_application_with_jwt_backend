package com.testlab.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testlab.DTO.RoleRequestDTO;
import com.testlab.DTO.RoleResponseDTO;
import com.testlab.services.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoleController {
	  private final RoleService roleService;

	    @PostMapping("/roles")
	    public ResponseEntity<RoleResponseDTO> createRole(@Validated @RequestBody RoleRequestDTO dto) {
	        RoleResponseDTO response = roleService.createRole(dto);
	        return ResponseEntity.created(URI.create("/api/roles/" + response.getRoleId())).body(response);
	    }

	    @GetMapping("/roles")
	    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
	        return ResponseEntity.ok(roleService.getAllRoles());
	    }

	    @GetMapping("/roles/user/{userId}")
	    public ResponseEntity<List<RoleResponseDTO>> getRolesByUser(@PathVariable Long userId) {
	        return ResponseEntity.ok(roleService.getRolesByUserId(userId));
	    }

	    @DeleteMapping("/roles/{id}")
	    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
	        roleService.deleteRole(id);
	        return ResponseEntity.noContent().build();
	    }
}
