package com.stockup.StockUp.Manager.controller;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.Docs.RoleControllerDocs;
import com.stockup.StockUp.Manager.dto.roles.request.RoleDTO;
import com.stockup.StockUp.Manager.dto.roles.request.RoleUpdateDTO;
import com.stockup.StockUp.Manager.dto.roles.response.RoleWithUsersDTO;
import com.stockup.StockUp.Manager.mapper.RoleMapper;
import com.stockup.StockUp.Manager.model.security.Role;
import com.stockup.StockUp.Manager.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController implements RoleControllerDocs {
	
	private final RoleService roleService;
	private final RoleMapper roleMapper;
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<Role> createRole(@Valid @RequestBody RoleDTO createDto) {
		try {
			Role role = roleService.createRole(createDto);
			AuditLogger.log("ROLE_CREATE", getCurrentUser(), "SUCCESS", "Role created: " + createDto.getName());
			return ResponseEntity.status(HttpStatus.CREATED).body(role);
		} catch (Exception e) {
			AuditLogger.log("ROLE_CREATE", getCurrentUser(), "FAILED", "Error creating role: " + e.getMessage());
			throw new RuntimeException("Error creating role", e);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update")
	public ResponseEntity<Role> updateRole(@Valid @RequestBody RoleUpdateDTO updateDto) {
		try {
			Role role = roleService.updateRole(updateDto);
			AuditLogger.log("ROLE_UPDATE", getCurrentUser(), "SUCCESS",
				"Role updated from [" + updateDto.getOldName() + "] to [" + updateDto.getNewName() + "]");
			return ResponseEntity.ok(role);
		} catch (Exception e) {
			AuditLogger.log("ROLE_UPDATE", getCurrentUser(), "FAILED", "Error updating role: " + e.getMessage());
			throw new RuntimeException("Error updating role", e);
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/id/{id}")
	public ResponseEntity<Role> getRoleById(@PathVariable UUID id) {
		Role role = roleService.getRoleById(id);
		return ResponseEntity.ok(role);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{name}")
	public ResponseEntity<Role> getRoleByName(@PathVariable String name) {
		Role role = roleService.getRoleByName(name);
		return ResponseEntity.ok(role);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{name}")
	public ResponseEntity<Void> deleteRole(@PathVariable String name) {
		try {
			roleService.deleteRole(name);
			AuditLogger.log("ROLE_DELETE", getCurrentUser(), "SUCCESS", "Role deleted: " + name);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			AuditLogger.log("ROLE_DELETE", getCurrentUser(), "FAILED", "Error deleting role: " + e.getMessage());
			throw new RuntimeException("Error deleting role", e);
		}
	}
	
	@Override
	@GetMapping("/list")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<RoleDTO>> listRoles(Pageable pageable) {
		Page<Role> rolesPage = roleService.getAllRoles(pageable);
		Page<RoleDTO> dtoPage = rolesPage.map(roleMapper::toDTO);
		return ResponseEntity.ok(dtoPage);
	}
	
	@Override
	@GetMapping("/list-with-users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<RoleWithUsersDTO>> listRolesWithUsers(Pageable pageable) {
		Page<Role> rolesPage = roleService.getAllRoles(pageable);
		Page<RoleWithUsersDTO> dtoPage = rolesPage.map(roleMapper::toWithUsersDTO);
		return ResponseEntity.ok(dtoPage);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{roleName}/permissions/assign")
	public ResponseEntity<Role> assignPermissions(
		@PathVariable String roleName,
		@Valid @RequestBody List<String> permissionDescriptions
	) {
		Role updatedRole = roleService.assignPermissions(roleName, permissionDescriptions);
		return ResponseEntity.ok(updatedRole);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{roleName}/permissions/remove")
	public ResponseEntity<Role> removePermissions(
		@PathVariable String roleName,
		@Valid @RequestBody List<String> permissionDescriptions
	) {
		Role updatedRole = roleService.removePermissions(roleName, permissionDescriptions);
		return ResponseEntity.ok(updatedRole);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{roleName}/permissions")
	public ResponseEntity<List<String>> getRolePermissions(@PathVariable String roleName) {
		List<String> permissions = roleService.getRolePermissions(roleName);
		return ResponseEntity.ok(permissions);
	}
}