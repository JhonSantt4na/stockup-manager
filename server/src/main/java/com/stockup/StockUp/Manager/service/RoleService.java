package com.stockup.StockUp.Manager.service;

import com.stockup.StockUp.Manager.dto.Roles.RoleCreateDTO;
import com.stockup.StockUp.Manager.dto.Roles.RoleUpdateDTO;
import com.stockup.StockUp.Manager.model.security.Permission;
import com.stockup.StockUp.Manager.repository.PermissionRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RoleService {
	
	private static final Logger logger = LoggerFactory.getLogger(RoleService.class);
	
	private final PermissionRepository permissionRepository;
	
	public Permission createRole(RoleCreateDTO dto) {
		logger.info("Creating New Permission [{}]", dto.getDescription());
		permissionRepository.findByDescription(dto.getDescription())
			.ifPresent(r -> {
				logger.warn("Attempt to create duplicate roller [{}]", dto.getDescription());
				throw new IllegalArgumentException("Role already exists: " + dto.getDescription());
			});
		
		Permission permission = new Permission();
		permission.setDescription(dto.getDescription());
		permission.setCreatedAt(LocalDateTime.now());
		logger.info("Permission successfully created [{}]", dto.getDescription());
		return permissionRepository.save(permission);
	}
	
	public Permission updateRole(RoleUpdateDTO dto) {
		logger.info("Updating permission from [{}] to [{}]", dto.getOldDescription(), dto.getNewDescription());
		
		Permission permission = getRoleByDescription(dto.getOldDescription());
		
		if (!dto.getOldDescription().equals(dto.getNewDescription())) {
			permissionRepository.findByDescription(dto.getNewDescription())
				.ifPresent(existingPermission -> {
					logger.warn("Cannot update permission [{}] to [{}] - new description already exists",
						dto.getOldDescription(), dto.getNewDescription());
					throw new IllegalArgumentException("Permission '" + dto.getNewDescription() + "' already exists");
				});
		}
		
		permission.setDescription(dto.getNewDescription());
		permission.setUpdatedAt(LocalDateTime.now());
		
		Permission updated = permissionRepository.save(permission);
		logger.info("Permission successfully updated from [{}] to [{}]",
			dto.getOldDescription(), dto.getNewDescription());
		return updated;
	}
	
	public void deleteRole(String description) {
		logger.info("Deleting Permission [{}]", description);
		Permission permission = getRoleByDescription(description);
		permission.setDeletedAt(LocalDateTime.now());
		permission.disable();
		permissionRepository.save(permission);
		logger.info("Permission successfully disabled [{}]", description);
	}
	
	public List<Permission> getAllRoles() {
		logger.debug("Listing all permissions");
		List<Permission> permission = permissionRepository.findAll();
		logger.info("Total permissions found [{}]", permission.size());
		return permission;
	}
	
	public Permission getRoleByDescription(String description) {
		logger.debug("Seeking permissions for description [{}]", description);
		return permissionRepository.findByDescription(description)
			.orElseThrow(() -> {
				logger.warn("Permission not found [{}]", description);
				return new IllegalArgumentException("Role not found: " + description);
			});
	}
}