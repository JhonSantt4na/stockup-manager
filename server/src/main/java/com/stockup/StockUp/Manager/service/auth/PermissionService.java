package com.stockup.StockUp.Manager.service.auth;

import com.stockup.StockUp.Manager.dto.Auth.permission.request.PermissionCreateDTO;
import com.stockup.StockUp.Manager.dto.Auth.permission.request.PermissionUpdateDTO;
import com.stockup.StockUp.Manager.dto.Auth.permission.response.PermissionWithRolesDTO;
import com.stockup.StockUp.Manager.dto.Auth.permission.response.RoleInPermissionDTO;
import com.stockup.StockUp.Manager.exception.DuplicateResourceException;
import com.stockup.StockUp.Manager.model.security.Permission;
import com.stockup.StockUp.Manager.repository.auth.PermissionRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

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
				throw new DuplicateResourceException("Permission already exists: " + dto.getDescription());
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
					throw new DuplicateResourceException("Permission '" + dto.getNewDescription() + "' already exists");
				});
		}
		
		permission.setDescription(dto.getNewDescription());
		permission.setUpdatedAt(LocalDateTime.now());
		permission.setEnabled(dto.getEnabled());
		Permission updated = permissionRepository.save(permission);
		logger.info("Permission successfully updated from [{}] to [{}]",
			dto.getOldDescription(), dto.getNewDescription());
		return updated;
	}
	
	public void deletePermission(String description) {
		logger.info("Deleting Permission [{}]", description);
		Permission permission = getPermissionByDescription(description);
		permissionRepository.delete(permission);
		logger.info("Permission successfully deleted [{}]", description);
	}
	
	public Page<PermissionWithRolesDTO> getAllActivePermissions(Pageable pageable) {
		logger.debug("Listing all active permissions with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
		
		Page<Permission> permissionList = permissionRepository.findByEnabledTrue(pageable);
		logger.info("Total active permissions found [{}]", permissionList.getTotalElements());
		
		return permissionList.map(permission -> new PermissionWithRolesDTO(
			permission.getId(),
			permission.getDescription(),
			permission.isEnabled(),
			permission.getRoles().stream()
				.map(r -> new RoleInPermissionDTO(r.getId(), r.getName()))
				.collect(Collectors.toList())
		));
	}
	
	public Page<PermissionWithRolesDTO> getAllPermissions(Pageable pageable) {
		logger.debug("Listing all permissions with pagination: page={}, size={}",
			pageable.getPageNumber(), pageable.getPageSize());
		
		Page<Permission> permissionList = permissionRepository.findAll(pageable);
		logger.info("Total permissions found [{}]", permissionList.getTotalElements());
		
		return permissionList.map(permission -> new PermissionWithRolesDTO(
			permission.getId(),
			permission.getDescription(),
			permission.isEnabled(),
			permission.getRoles().stream()
				.map(r -> new RoleInPermissionDTO(r.getId(), r.getName()))
				.collect(Collectors.toList())
		));
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