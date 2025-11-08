package com.stockup.StockUp.Manager.repository.auth;

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
	
	@Query("SELECT u FROM User u WHERE (:role IS NULL OR :role = '' OR LOWER(u.roles) LIKE LOWER(CONCAT('%', :role, '%')))")
	Page<User> findByRole(@Param("role") String role, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE (LOWER(u.username) LIKE :term OR LOWER(u.email) LIKE :term)")
	Page<User> findBySearch(@Param("term") String term, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE (LOWER(u.username) LIKE :term OR LOWER(u.email) LIKE :term) AND u.enabled = :enabled")
	Page<User> findBySearchAndEnabled(@Param("term") String term, @Param("enabled") boolean enabled, Pageable pageable);
	
	Page<User> findAllByEnabled(boolean enabled, Pageable pageable);
	
	Page<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String email, Pageable pageable);
	Page<User> findByEnabled(Boolean enabled, Pageable pageable);
	
	boolean existsByUsername(String username);
	boolean existsByEmailAndIdNot(String email, UUID id);
	boolean existsByEmail(String email);
}