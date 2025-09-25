package com.stockup.StockUp.Manager.repository;

import com.stockup.StockUp.Manager.entity.security.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionsRepository extends JpaRepository<Permission, Long> {
	Optional<Permission> findByDescription(String description);
}