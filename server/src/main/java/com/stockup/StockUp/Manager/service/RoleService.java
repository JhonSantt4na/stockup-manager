package com.stockup.StockUp.Manager.service;

import com.stockup.StockUp.Manager.dto.Roles.RoleCreateDTO;
import com.stockup.StockUp.Manager.model.security.Permission;
import com.stockup.StockUp.Manager.repository.PermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RoleService {
	
	private final PermissionRepository permissionRepository;
	
	public Permission createRole(RoleCreateDTO dto) {
		permissionRepository.findByDescription(dto.getDescription())
			.ifPresent(r -> {
				throw new IllegalArgumentException("Role already exists: " + dto.getDescription());
			});
		
		Permission permission = new Permission();
		permission.setDescription(dto.getDescription());
		permission.setCreatedAt(LocalDateTime.now());
		
		return permissionRepository.save(permission);
	}
	
	public List<Permission> getAllRoles() {
		return permissionRepository.findAll();
	}
	
	public Permission getRoleByDescription(String description) {
		return permissionRepository.findByDescription(description)
			.orElseThrow(() -> new IllegalArgumentException("Role not found: " + description));
	}
}