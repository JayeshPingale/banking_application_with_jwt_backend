package com.testlab.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testlab.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

	   Optional<User> findByUserName(String userName);
	    boolean existsByUserName(String userName);
}
