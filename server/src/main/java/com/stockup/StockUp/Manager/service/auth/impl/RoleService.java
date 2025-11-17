package com.stockup.StockUp.Manager.service.auth.impl;

import com.stockup.StockUp.Manager.dto.Auth.roles.request.RoleDTO;
import com.stockup.StockUp.Manager.dto.Auth.roles.request.RoleUpdateDTO;
import com.stockup.StockUp.Manager.model.security.Permission;
import com.stockup.StockUp.Manager.model.security.Role;
import com.stockup.StockUp.Manager.repository.auth.PermissionRepository;
import com.stockup.StockUp.Manager.repository.auth.RoleRepository;
import com.stockup.StockUp.Manager.service.auth.IRoleService;
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
public class RoleService implements IRoleService {
	
	private static final Logger logger = LoggerFactory.getLogger(RoleService.class);
	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;
	
	@PostConstruct
	public void ensureBuiltInRoles() {
		List<String> builtInRoles = List.of("ADMIN", "USER");
		
		for (String roleName : builtInRoles) {
			roleRepository.findByName(roleName)
				.orElseGet(() -> {
					Role role = new Role();
					role.setName(roleName);
					role.setEnabled(true);
					role.setCreatedAt(LocalDateTime.now());
					logger.info("Created built-in role: {}", roleName);
					return roleRepository.save(role);
				});
		}
	}
	
	@Override
	public Role createRole(RoleDTO dto) {
		logger.info("Creating new role [{}]", dto.getName());
		
		roleRepository.findByName(dto.getName())
			.ifPresent(r -> {
				logger.warn("Attempt to create duplicate role [{}]", dto.getName());
				throw new IllegalArgumentException("Role already exists: " + dto.getName());
			});
		
		Role role = new Role();
		role.setName(dto.getName());
		role.setEnabled(true);
		role.setCreatedAt(LocalDateTime.now());
		
		Role created = roleRepository.save(role);
		logger.info("Role successfully created [{}]", created.getName());
		return created;
	}
	
	@Override
	public Role updateRole(RoleUpdateDTO dto) {
		logger.info("Updating role from [{}] to [{}]", dto.getOldName(), dto.getNewName());
		
		Role role = getRoleByName(dto.getOldName());
		
		if (!dto.getOldName().equals(dto.getNewName())) {
			roleRepository.findByName(dto.getNewName())
				.ifPresent(r -> {
					logger.warn("Role update aborted — '{}' already exists", dto.getNewName());
					throw new IllegalArgumentException("Role '" + dto.getNewName() + "' already exists");
				});
			role.setName(dto.getNewName());
		}
		
		if (dto.getEnabled() != null) {
			role.setEnabled(dto.getEnabled());
		}
		
		role.setUpdatedAt(LocalDateTime.now());
		
		Role updated = roleRepository.save(role);
		logger.info("Role updated successfully [{}]", updated.getName());
		return updated;
	}
	
	@Override
	public Role getRoleById(UUID id) {
		logger.debug("Fetching role by ID [{}]", id);
		return roleRepository.findById(id)
			.orElseThrow(() -> {
				logger.warn("Role not found with ID [{}]", id);
				return new IllegalArgumentException("Role not found with ID: " + id);
			});
	}
	
	@Override
	public Role getRoleByName(String name) {
		logger.debug("Fetching role by name [{}]", name);
		return roleRepository.findByName(name)
			.orElseThrow(() -> {
				logger.warn("Role not found [{}]", name);
				return new IllegalArgumentException("Role not found: " + name);
			});
	}
	
	@Override
	public void deleteRole(String name) {
		logger.info("Deleting role [{}]", name);
		Role role = getRoleByName(name);
		roleRepository.delete(role);
		logger.info("Role deleted successfully [{}]", name);
	}
	
	@Override
	public Page<Role> getAllRoles(Pageable pageable, String search) {
		logger.debug("Listing roles with pagination, search='{}'", search);
		
		if (search == null || search.isBlank()) {
			return roleRepository.findAll(pageable);
		}
		return roleRepository.findByEnabledTrueAndNameContainingIgnoreCase(search.trim(), pageable);
	}
	
	@Override
	public Page<Role> getAllRolesWithUsers(Pageable pageable, String search) {
		// Se no futuro você quiser fazer JOIN FETCH com usuários, é aqui.
		return getAllRoles(pageable, search);
	}
	
	@Override
	public Role assignPermissions(String roleName, List<String> permissionDescriptions) {
		logger.info("Assigning permissions {} → role [{}]", permissionDescriptions, roleName);
		
		Role role = getRoleByName(roleName);
		
		List<Permission> permissions = permissionRepository.findAllByDescriptionIn(permissionDescriptions);
		
		if (permissions.isEmpty()) {
			logger.warn("No valid permissions found for assignment to role [{}]", roleName);
			throw new IllegalArgumentException("No valid permissions found");
		}
		
		Set<Permission> updatedSet = new HashSet<>(role.getPermissions());
		updatedSet.addAll(permissions);
		role.setPermissions(new ArrayList<>(updatedSet));
		
		Role updated = roleRepository.save(role);
		logger.info("Assigned {} permissions to role [{}]", permissions.size(), roleName);
		return updated;
	}
	
	@Override
	public Role removePermissions(String roleName, List<String> permissionDescriptions) {
		logger.info("Removing permissions {} → role [{}]", permissionDescriptions, roleName);
		
		Role role = getRoleByName(roleName);
		
		role.getPermissions().removeIf(p ->
			permissionDescriptions.contains(p.getDescription())
		);
		
		Role updated = roleRepository.save(role);
		logger.info("Permissions removed from role [{}]. Count: {}", roleName, permissionDescriptions.size());
		return updated;
	}
	
	@Override
	public List<String> getRolePermissions(String roleName) {
		Role role = getRoleByName(roleName);
		
		List<String> permissions = role.getPermissions().stream()
			.map(Permission::getDescription)
			.collect(Collectors.toList());
		
		logger.debug("Role [{}] has {} permissions", roleName, permissions.size());
		return permissions;
	}
}
