package com.stockup.StockUp.Manager.service;

import com.stockup.StockUp.Manager.dto.roles.request.RoleDTO;
import com.stockup.StockUp.Manager.dto.roles.request.RoleUpdateDTO;
import com.stockup.StockUp.Manager.model.security.Permission;
import com.stockup.StockUp.Manager.model.security.Role;
import com.stockup.StockUp.Manager.repository.PermissionRepository;
import com.stockup.StockUp.Manager.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleService {
	
	private static final Logger logger = LoggerFactory.getLogger(RoleService.class);
	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;
	
	@PostConstruct
	public void ensureBuiltInRoles() {
		List<String> builtInRoles = List.of("ADMIN", "USER");
		
		for (String roleName : builtInRoles) {
			if (roleRepository.findByName(roleName).isEmpty()) {
				Role role = new Role();
				role.setName(roleName);
				role.setCreatedAt(LocalDateTime.now());
				roleRepository.save(role);
				logger.info("Created built-in role: {}", roleName);
			}
		}
	}
	
	public Role createRole(RoleDTO createDto) {
		logger.info("Creating New Role [{}]", createDto.getName());
		
		roleRepository.findByName(createDto.getName())
			.ifPresent(r -> {
				logger.warn("Attempt to create duplicate role [{}]", r.getName());
				throw new IllegalArgumentException("Role already exists: " + r.getName());
			});
		
		Role role = new Role();
		role.setName(createDto.getName());
		role.setCreatedAt(LocalDateTime.now());
		
		logger.info("Role successfully created [{}]", createDto.getName());
		return roleRepository.save(role);
	}
	
	public Role updateRole(RoleUpdateDTO updateDto) {
		logger.info("Updating role from [{}] to [{}]", updateDto.getOldName(), updateDto.getNewName());
		
		Role role = getRoleByName(updateDto.getOldName());
		
		if (!updateDto.getOldName().equals(updateDto.getNewName())) {
			roleRepository.findByName(updateDto.getNewName())
				.ifPresent(existingRole -> {
					logger.warn("Cannot update role [{}] to [{}] - new name already exists",
						updateDto.getOldName(), updateDto.getNewName());
					throw new IllegalArgumentException("Role '" + updateDto.getNewName() + "' already exists");
				});
		}
		
		if (updateDto.getEnabled() != null) {
			role.setEnabled(updateDto.getEnabled());
		}
		
		role.setName(updateDto.getNewName());
		role.setUpdatedAt(LocalDateTime.now());
		
		Role updated = roleRepository.save(role);
		logger.info("Role successfully updated from [{}] to [{}]",
			updateDto.getOldName(), updateDto.getNewName());
		return updated;
	}
	
	public Role getRoleById(UUID id) {
		logger.debug("Seeking role by ID [{}]", id);
		return roleRepository.findById(id)
			.orElseThrow(() -> {
				logger.warn("Role not found with ID [{}]", id);
				return new IllegalArgumentException("Role not found with ID: " + id);
			});
	}
	
	public Role getRoleByName(String name) {
		logger.debug("Seeking role for name [{}]", name);
		return roleRepository.findByName(name)
			.orElseThrow(() -> {
				logger.warn("Role not found [{}]", name);
				return new IllegalArgumentException("Role not found: " + name);
			});
	}
	
	public void deleteRole(String name) {
		logger.info("Deleting Role [{}]", name);
		Role role = getRoleByName(name);
		roleRepository.delete(role);
		logger.info("Role successfully deleted [{}]", name);
	}
	
	public Page<Role> getAllRoles(Pageable pageable, String search) {
		if (search == null || search.isBlank()) {
			return roleRepository.findAll(pageable);
		} else {
			return roleRepository.findByEnabledTrueAndNameContainingIgnoreCase(search.trim(), pageable);
		}
	}
	
	public Page<Role> getAllRolesWithUsers(Pageable pageable, String search) {
		return getAllRoles(pageable, search);
	}
	
	public Role assignPermissions(String roleName, List<String> permissionDescriptions) {
		logger.info("Adding permissions {} to role [{}]", permissionDescriptions, roleName);
		
		Role role = getRoleByName(roleName);
		
		List<Permission> permissionsToAdd = permissionRepository.findAllByDescriptionIn(permissionDescriptions);
		
		if (permissionsToAdd.isEmpty()) {
			logger.warn("No valid permissions found to add to role [{}]", roleName);
			throw new IllegalArgumentException("No valid permissions found to add");
		}
		
		Set<Permission> currentPermissions = new HashSet<>(role.getPermissions());
		currentPermissions.addAll(permissionsToAdd);
		role.setPermissions(new ArrayList<>(currentPermissions));
		
		Role updatedRole = roleRepository.save(role);
		logger.info("Added {} permissions to role [{}]", permissionsToAdd.size(), roleName);
		return updatedRole;
	}
	
	public Role removePermissions(String roleName, List<String> permissionDescriptions) {
		logger.info("Removing permissions {} from role [{}]", permissionDescriptions, roleName);
		
		Role role = getRoleByName(roleName);
		
		List<Permission> currentPermissions = role.getPermissions();
		currentPermissions.removeIf(permission -> permissionDescriptions.contains(permission.getDescription()));
		
		Role updatedRole = roleRepository.save(role);
		logger.info("Removed {} permissions from role [{}]", permissionDescriptions.size(), roleName);
		return updatedRole;
	}
	
	public List<String> getRolePermissions(String roleName) {
		Role role = getRoleByName(roleName);
		
		List<String> permissions = role.getPermissions()
			.stream()
			.map(Permission::getDescription)
			.collect(Collectors.toList());
		
		logger.debug("Role [{}] has {} permissions", roleName, permissions.size());
		return permissions;
	}
}