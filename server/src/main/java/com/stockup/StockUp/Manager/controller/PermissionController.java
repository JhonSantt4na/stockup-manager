package com.stockup.StockUp.Manager.controller;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.Docs.PermissionControllerDocs;
import com.stockup.StockUp.Manager.dto.permission.PermissionCreateDTO;
import com.stockup.StockUp.Manager.dto.permission.PermissionUpdateDTO;
import com.stockup.StockUp.Manager.exception.DuplicateResourceException;
import com.stockup.StockUp.Manager.model.security.Permission;
import com.stockup.StockUp.Manager.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController implements PermissionControllerDocs {
	
	private final PermissionService permissionService;
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<Permission> createPermission(@Valid @RequestBody PermissionCreateDTO dto) {
		try {
			Permission permission = permissionService.createPermission(dto);
			AuditLogger.log("PERMISSION_CREATE", getCurrentUser(), "SUCCESS", "Permission created: " + dto.getDescription());
			return ResponseEntity.status(HttpStatus.CREATED).body(permission);
		} catch (DuplicateResourceException e) {
			AuditLogger.log("PERMISSION_CREATE", getCurrentUser(), "FAILED", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (Exception e) {
			AuditLogger.log("PERMISSION_CREATE", getCurrentUser(), "FAILED", "Error creating permission: " + e.getMessage());
			throw new RuntimeException("Error creating permission", e);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update")
	public ResponseEntity<Permission> updatePermission(@Valid @RequestBody PermissionUpdateDTO dto) {
		try {
			Permission permission = permissionService.updatePermission(dto);
			AuditLogger.log("PERMISSION_UPDATE", getCurrentUser(), "SUCCESS",
				"Permission updated from [" + dto.getOldDescription() + "] to [" + dto.getNewDescription() + "]");
			return ResponseEntity.ok(permission);
		} catch (DuplicateResourceException e) {
			AuditLogger.log("PERMISSION_CREATE", getCurrentUser(), "FAILED", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (Exception e) {
			AuditLogger.log("PERMISSION_UPDATE", getCurrentUser(), "FAILED", "Error updating permission: " + e.getMessage());
			throw new RuntimeException("Error updating permission", e);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{description}")
	public ResponseEntity<Permission> getPermissionByDescription(@PathVariable String description) {
		Permission permission = permissionService.getPermissionByDescription(description);
		return ResponseEntity.ok(permission);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{description}")
	public ResponseEntity<Void> deletePermission(@PathVariable String description) {
		try {
			permissionService.deletePermission(description);
			AuditLogger.log("PERMISSION_DELETE", getCurrentUser(), "SUCCESS", "Permission deleted: " + description);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			AuditLogger.log("PERMISSION_DELETE", getCurrentUser(), "FAILED", "Error deleting permission: " + e.getMessage());
			throw new RuntimeException("Error deleting permission", e);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/list")
	public ResponseEntity<Page<Permission>> getAllPermissions(Pageable pageable) {
		Page<Permission> permissionsPage = permissionService.getAllPermission(pageable);
		return ResponseEntity.ok(permissionsPage);
	}
}