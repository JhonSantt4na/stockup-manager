package com.stockup.StockUp.Manager.repository;

import com.stockup.StockUp.Manager.model.security.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {
	Optional<Permission> findByDescription(String description);
	List<Permission> findAllByDescriptionIn(List<String> descriptions);
	boolean existsByDescription(String description);
}