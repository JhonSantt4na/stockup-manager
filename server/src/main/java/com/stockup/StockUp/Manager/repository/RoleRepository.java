package com.stockup.StockUp.Manager.repository;

import com.stockup.StockUp.Manager.model.security.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
	
	Optional<Role> findByName(String name);
	List<Role> findAllByNameIn(List<String> names);
	boolean existsByName(String name);
	Page<Role> findByEnabledTrue(Pageable pageable);
	Page<Role> findByEnabledTrueAndNameContainingIgnoreCase(String search, Pageable pageable);
}