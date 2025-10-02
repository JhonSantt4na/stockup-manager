package com.stockup.StockUp.Manager.controller;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.Docs.PermissionControllerDocs;
import com.stockup.StockUp.Manager.dto.security.permission.PermissionCreateDTO;
import com.stockup.StockUp.Manager.dto.security.permission.PermissionUpdateDTO;
import com.stockup.StockUp.Manager.model.security.Permission;
import com.stockup.StockUp.Manager.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController implements PermissionControllerDocs {
	
	private final PermissionService permissionService;
	
	@Override
	@PostMapping("/create")
	public ResponseEntity<Permission> createPermission(@Valid @RequestBody PermissionCreateDTO dto) {
		try {
			Permission permission = permissionService.createPermission(dto);
			AuditLogger.log("PERMISSION_CREATE", getCurrentUser(), "SUCCESS", "Permission created: " + dto.getDescription());
			return ResponseEntity.status(HttpStatus.CREATED).body(permission);
			
		} catch (Exception e) {
			AuditLogger.log("PERMISSION_CREATE", getCurrentUser(), "FAILED", "Error creating permission: " + e.getMessage());
			throw new RuntimeException("Error creating permission", e);
		}
	}
	
	@Override
	@PutMapping("/update")
	public ResponseEntity<Permission> updatePermission(@Valid @RequestBody PermissionUpdateDTO dto) {
		try {
			Permission permission = permissionService.updatePermission(dto);
			AuditLogger.log("PERMISSION_UPDATE", getCurrentUser(), "SUCCESS",
				"Permission updated from [" + dto.getOldDescription() + "] to [" + dto.getNewDescription() + "]");
			return ResponseEntity.ok(permission);
		} catch (Exception e) {
			AuditLogger.log("PERMISSION_UPDATE", getCurrentUser(), "FAILED", "Error updating permission: " + e.getMessage());
			throw new RuntimeException("Error updating permission", e);
		}
	}
	
	@Override
	@GetMapping("/{description}")
	public ResponseEntity<Permission> getPermissionByDescription(@PathVariable String description) {
		Permission permission = permissionService.getPermissionByDescription(description);
		return ResponseEntity.ok(permission);
	}
	
	@Override
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
	@GetMapping("/list")
	public ResponseEntity<List<Permission>> listPermissions() {
		List<Permission> permissions = permissionService.getAllPermission();
		return ResponseEntity.ok(permissions);
	}
}