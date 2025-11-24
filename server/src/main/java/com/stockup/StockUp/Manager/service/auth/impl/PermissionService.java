package com.stockup.StockUp.Manager.service.auth.impl;

import com.stockup.StockUp.Manager.dto.Auth.permission.request.PermissionCreateDTO;
import com.stockup.StockUp.Manager.dto.Auth.permission.request.PermissionUpdateDTO;
import com.stockup.StockUp.Manager.dto.Auth.permission.response.PermissionWithRolesDTO;
import com.stockup.StockUp.Manager.dto.Auth.permission.response.RoleInPermissionDTO;
import com.stockup.StockUp.Manager.exception.DuplicateResourceException;
import com.stockup.StockUp.Manager.model.user.Permission;
import com.stockup.StockUp.Manager.repository.PermissionRepository;
import com.stockup.StockUp.Manager.service.auth.IPermissionService;
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
public class PermissionService implements IPermissionService {
	
	private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);
	
	private final PermissionRepository permissionRepository;
	
	@Override
	public Permission createPermission(PermissionCreateDTO dto) {
		logger.info("Request received to create permission [{}]", dto.getDescription());
		
		permissionRepository.findByDescription(dto.getDescription())
			.ifPresent(existing -> {
				logger.warn("Duplicate permission creation attempt [{}]", dto.getDescription());
				throw new DuplicateResourceException("Permission already exists: " + dto.getDescription());
			});
		
		Permission permission = new Permission();
		permission.setDescription(dto.getDescription());
		permission.setCreatedAt(LocalDateTime.now());
		
		Permission saved = permissionRepository.save(permission);
		
		logger.info("Permission created successfully [{}] (ID: {})", saved.getDescription(), saved.getId());
		return saved;
	}
	
	@Override
	public Permission updatePermission(PermissionUpdateDTO dto) {
		logger.info("Updating permission [{}] to new description [{}]", dto.getOldDescription(), dto.getNewDescription());
		
		Permission permission = getPermissionByDescription(dto.getOldDescription());
		
		if (!dto.getOldDescription().equalsIgnoreCase(dto.getNewDescription())) {
			permissionRepository.findByDescription(dto.getNewDescription())
				.ifPresent(existing -> {
					logger.warn("Permission update blocked: new description [{}] already exists", dto.getNewDescription());
					throw new DuplicateResourceException(
						"Permission '" + dto.getNewDescription() + "' already exists"
					);
				});
		}
		
		permission.setDescription(dto.getNewDescription());
		permission.setEnabled(dto.getEnabled());
		permission.setUpdatedAt(LocalDateTime.now());
		
		Permission updated = permissionRepository.save(permission);
		
		logger.info("Permission updated successfully [{}] → [{}]",
			dto.getOldDescription(), dto.getNewDescription());
		
		return updated;
	}
	
	@Override
	public void deletePermission(String description) {
		logger.info("Request received to delete permission [{}]", description);
		
		Permission permission = getPermissionByDescription(description);
		permissionRepository.delete(permission);
		
		logger.info("Permission deleted successfully [{}]", description);
	}
	
	@Override
	public Page<PermissionWithRolesDTO> getAllActivePermissions(Pageable pageable) {
		logger.debug("Fetching active permissions (page={}, size={})",
			pageable.getPageNumber(), pageable.getPageSize());
		
		Page<Permission> page = permissionRepository.findByEnabledTrue(pageable);
		
		logger.info("Active permissions found: {}", page.getTotalElements());
		
		return page.map(permission ->
			new PermissionWithRolesDTO(
				permission.getId(),
				permission.getDescription(),
				permission.isEnabled(),
				permission.getRoles()
					.stream()
					.map(role -> new RoleInPermissionDTO(role.getId(), role.getName()))
					.collect(Collectors.toList())
			)
		);
	}
	
	@Override
	public Page<PermissionWithRolesDTO> getAllPermissions(Pageable pageable) {
		logger.debug("Fetching all permissions (page={}, size={})",
			pageable.getPageNumber(), pageable.getPageSize());
		
		Page<Permission> page = permissionRepository.findAll(pageable);
		
		logger.info("Total permissions found: {}", page.getTotalElements());
		
		return page.map(permission ->
			new PermissionWithRolesDTO(
				permission.getId(),
				permission.getDescription(),
				permission.isEnabled(),
				permission.getRoles()
					.stream()
					.map(role -> new RoleInPermissionDTO(role.getId(), role.getName()))
					.collect(Collectors.toList())
			)
		);
	}
	
	@Override
	public Permission getPermissionByDescription(String description) {
		logger.debug("Looking up permission by description [{}]", description);
		
		return permissionRepository.findByDescription(description)
			.orElseThrow(() -> {
				logger.warn("Permission not found [{}]", description);
				return new IllegalArgumentException("Permission not found: " + description);
			});
	}
}
