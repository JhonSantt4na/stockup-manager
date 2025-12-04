package com.stockup.StockUp.Manager.controller.auth;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.auth.Docs.RoleControllerDocs;
import com.stockup.StockUp.Manager.dto.Auth.roles.request.RoleDTO;
import com.stockup.StockUp.Manager.dto.Auth.roles.request.RoleUpdateDTO;
import com.stockup.StockUp.Manager.dto.Auth.roles.response.RoleWithUsersDTO;
import com.stockup.StockUp.Manager.mapper.user.RoleMapper;
import com.stockup.StockUp.Manager.model.user.Role;
import com.stockup.StockUp.Manager.service.auth.IRoleService;
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
@RequestMapping("/api/v1/roles")
public class RoleController implements RoleControllerDocs {
	
	private final IRoleService roleService;
	private final RoleMapper roleMapper;
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/createAddress")
	public ResponseEntity<Role> createRole(@Valid @RequestBody RoleDTO createDto) {
		Role role = roleService.createRole(createDto);
		AuditLogger.log("ROLE_CREATE", getCurrentUser(), "SUCCESS", "Role created: " + createDto.getName());
		return ResponseEntity.status(HttpStatus.CREATED).body(role);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updateAddress")
	public ResponseEntity<Role> updateRole(@Valid @RequestBody RoleUpdateDTO updateDto) {
		Role role = roleService.updateRole(updateDto);
		AuditLogger.log("ROLE_UPDATE", getCurrentUser(), "SUCCESS",
			"Role updated from [" + updateDto.getOldName() + "] to [" + updateDto.getNewName() + "]");
		return ResponseEntity.ok(role);
	}
	
	@Override
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
		roleService.deleteRole(name);
		AuditLogger.log("ROLE_DELETE", getCurrentUser(), "SUCCESS", "Role deleted: " + name);
		return ResponseEntity.noContent().build();
	}
	
	@Override
	@GetMapping("/list")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<RoleWithUsersDTO>> listRoles(
		Pageable pageable,
		@RequestParam(required = false) String search
	) {
		Page<Role> rolesPage = roleService.getAllRolesWithUsers(pageable, search);
		Page<RoleWithUsersDTO> dtoPage = rolesPage.map(roleMapper::toWithUsersDTO);
		return ResponseEntity.ok(dtoPage);
	}
	
	@Override
	@GetMapping("/list-with-users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<RoleWithUsersDTO>> listRolesWithUsers(Pageable pageable, String search) {
		Page<Role> rolesPage = roleService.getAllRolesWithUsers(pageable, search);
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
