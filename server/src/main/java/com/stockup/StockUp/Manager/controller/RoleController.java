package com.stockup.StockUp.Manager.controller;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.Docs.RoleControllerDocs;
import com.stockup.StockUp.Manager.dto.Roles.RoleCreateDTO;
import com.stockup.StockUp.Manager.dto.Roles.RoleUpdateDTO;
import com.stockup.StockUp.Manager.model.security.Permission;
import com.stockup.StockUp.Manager.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
public class RoleController implements RoleControllerDocs {
	
	private final RoleService roleService;
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<Permission> createPermission(@Valid @RequestBody RoleCreateDTO dto) {
		try {
			Permission permission = roleService.createRole(dto);
			AuditLogger.log("ROLE_CREATE", getCurrentUser(), "SUCCESS", "Role created: " + dto.getDescription());
			return ResponseEntity.status(HttpStatus.CREATED).body(permission);
			
		} catch (Exception e) {
			AuditLogger.log("ROLE_CREATE", getCurrentUser(), "FAILED", "Error creating role: " + e.getMessage());
			throw new RuntimeException("Error creating role", e);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update")
	public ResponseEntity<Permission> updatePermission(@Valid @RequestBody RoleUpdateDTO dto) {
		try {
			Permission permission = roleService.updateRole(dto);
			AuditLogger.log("ROLE_UPDATE", getCurrentUser(), "SUCCESS",
				"Permission updated from [" + dto.getOldDescription() + "] to [" + dto.getNewDescription() + "]");
			return ResponseEntity.ok(permission);
		} catch (Exception e) {
			AuditLogger.log("ROLE_UPDATE", getCurrentUser(), "FAILED", "Error updating permission: " + e.getMessage());
			throw new RuntimeException("Error update permission", e);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{description}")
	public ResponseEntity<Permission> getDescriptionRoles(@PathVariable String description) {
		Permission permission = roleService.getRoleByDescription(description);
		return ResponseEntity.ok(permission);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{description}")
	public ResponseEntity<Void> deleteRole(@PathVariable String description) {
		try {
			roleService.deleteRole(description);
			AuditLogger.log("ROLE_DELETE", getCurrentUser(), "SUCCESS", "Role deleted: " + description);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			AuditLogger.log("ROLE_DELETE", getCurrentUser(), "FAILED", "Error deleting role: " + e.getMessage());
			throw new RuntimeException("Error deleting role", e);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/listAll")
	public ResponseEntity<List<Permission>> listRoles() {
		List<Permission> roles = roleService.getAllRoles();
		return ResponseEntity.ok(roles);
	}
}