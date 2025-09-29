package com.testlab.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testlab.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {



	Optional<Role> findByRoleName(String roleName);

}
