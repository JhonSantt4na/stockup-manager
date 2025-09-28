package com.stockup.StockUp.Manager.contorller;

import com.stockup.StockUp.Manager.dto.Roles.RoleCreateDTO;
import com.stockup.StockUp.Manager.dto.security.response.UserResponseDTO;
import com.stockup.StockUp.Manager.model.security.Permission;
import com.stockup.StockUp.Manager.repository.PermissionRepository;
import com.stockup.StockUp.Manager.repository.UserRepository;
import com.stockup.StockUp.Manager.service.RoleService;
import com.stockup.StockUp.Manager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
	
	private final RoleService roleService;
	private final UserService userService;
	
	private final UserRepository userRepository;
	private final PermissionRepository permissionRepository;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<Permission> createRole(@Valid @RequestBody RoleCreateDTO dto) {
		Permission role = roleService.createRole(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(role);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<Permission>> listRoles() {
		List<Permission> roles = roleService.getAllRoles();
		return ResponseEntity.ok(roles);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/assign/{username}")
	public ResponseEntity<UserResponseDTO> assignRoles(
		@PathVariable String username,
		@Valid @RequestBody List<String> roleNames
	) {
		UserResponseDTO updatedUser = userService.assignRoles(username, roleNames);
		return ResponseEntity.ok(updatedUser);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/user/{username}")
	public ResponseEntity<List<String>> getUserRoles(@PathVariable String username) {
		List<String> roles = userService.getUserRoles(username);
		return ResponseEntity.ok(roles);
	}
}
