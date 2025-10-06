package com.stockup.StockUp.Manager.repository;

import com.stockup.StockUp.Manager.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
	@Query("SELECT u FROM User u WHERE u.username = :username AND u.deletedAt IS NULL")
	Optional<User> findByUsername(@Param("username") String username);
	
	@Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :role AND u.deletedAt IS NULL")
	Page<User> findAllByRoles_Name(String roleName, Pageable pageable);
	
	boolean existsByUsername(String username);
	boolean existsByEmailAndIdNot(String email, UUID id);
	boolean existsByEmail(String email);
}