package com.stockup.StockUp.Manager.service;

import com.stockup.StockUp.Manager.dto.security.permission.PermissionCreateDTO;
import com.stockup.StockUp.Manager.dto.security.permission.PermissionUpdateDTO;
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
public class PermissionService {
	
	private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);
	
	private final PermissionRepository permissionRepository;
	
	public Permission createPermission(PermissionCreateDTO dto) {
		logger.info("Creating New Permission [{}]", dto.getDescription());
		permissionRepository.findByDescription(dto.getDescription())
			.ifPresent(r -> {
				logger.warn("Attempt to create duplicate permission [{}]", dto.getDescription());
				throw new IllegalArgumentException("Permission already exists: " + dto.getDescription());
			});
		
		Permission permission = new Permission();
		permission.setDescription(dto.getDescription());
		permission.setCreatedAt(LocalDateTime.now());
		logger.info("Permission successfully created [{}]", dto.getDescription());
		return permissionRepository.save(permission);
	}
	
	public Permission updatePermission(PermissionUpdateDTO dto) {
		logger.info("Updating permission from [{}] to [{}]", dto.getOldDescription(), dto.getNewDescription());
		
		Permission permission = getPermissionByDescription(dto.getOldDescription());
		
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
	
	public void deletePermission(String description) {
		logger.info("Deleting Permission [{}]", description);
		Permission permission = getPermissionByDescription(description);
		permission.setDeletedAt(LocalDateTime.now());
		permission.disable();
		permissionRepository.save(permission);
		logger.info("Permission successfully disabled [{}]", description);
	}
	
	public List<Permission> getAllPermission() {
		logger.debug("Listing all permissions");
		List<Permission> permission = permissionRepository.findAll();
		logger.info("Total permissions found [{}]", permission.size());
		return permission;
	}
	
	public Permission getPermissionByDescription(String description) {
		logger.debug("Seeking permissions for description [{}]", description);
		return permissionRepository.findByDescription(description)
			.orElseThrow(() -> {
				logger.warn("Permission not found [{}]", description);
				return new IllegalArgumentException("Permission not found: " + description);
			});
	}
}