package com.stockup.StockUp.Manager.controller.auth;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.auth.Docs.PermissionControllerDocs;
import com.stockup.StockUp.Manager.dto.Auth.permission.request.PermissionCreateDTO;
import com.stockup.StockUp.Manager.dto.Auth.permission.request.PermissionUpdateDTO;
import com.stockup.StockUp.Manager.dto.Auth.permission.response.PermissionWithRolesDTO;
import com.stockup.StockUp.Manager.exception.DuplicateResourceException;
import com.stockup.StockUp.Manager.model.security.Permission;
import com.stockup.StockUp.Manager.service.auth.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/api/permissions")
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
	@PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
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
	public ResponseEntity<Page<PermissionWithRolesDTO>> listPermissions(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "description,asc") String[] sort
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.by(sort[0])));
		Page<PermissionWithRolesDTO> response = permissionService.getAllPermissions(pageable);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/listActive")
	public ResponseEntity<Page<PermissionWithRolesDTO>> getAllPermissionsIsActive(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "description,asc") String[] sort
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort[1].toUpperCase()), sort[0]));
		Page<PermissionWithRolesDTO> response = permissionService.getAllActivePermissions(pageable);
		return ResponseEntity.ok(response);
	}
}