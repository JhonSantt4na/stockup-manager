package com.stockup.StockUp.Manager.controller;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.Docs.UserControllerDocs;
import com.stockup.StockUp.Manager.dto.ChangePasswordRequestDTO;
import com.stockup.StockUp.Manager.dto.security.request.RegisterRequestDTO;
import com.stockup.StockUp.Manager.dto.security.response.UserResponseDTO;
import com.stockup.StockUp.Manager.model.User;
import com.stockup.StockUp.Manager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {
	
	private final UserService service;
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/register")
	public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO credentials) {
		try {
			UserResponseDTO response = service.registerUser(credentials);
			AuditLogger.log("USER_REGISTER", getCurrentUser(), "SUCCESS", "User registered: " + credentials.getUsername());
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
			
		} catch (Exception e) {
			AuditLogger.log("USER_REGISTER", getCurrentUser(), "FAILED", "Error registering user: " + e.getMessage());
			throw new RuntimeException("Error registering user", e);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update")
	public ResponseEntity<UserResponseDTO> updated(@Valid @RequestBody RegisterRequestDTO credentials) {
		try {
			UserResponseDTO response = service.updatedUser(credentials);
			AuditLogger.log("USER_UPDATE", getCurrentUser(), "SUCCESS", "User updated: " + credentials.getUsername());
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			AuditLogger.log("USER_UPDATE", getCurrentUser(), "FAILED", "Error updating user: " + e.getMessage());
			throw new RuntimeException("Error updating user", e);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/find/{username}")
	public ResponseEntity<UserResponseDTO> findByUsername(@PathVariable String username) {
		UserResponseDTO user = service.findUser(username);
		return ResponseEntity.ok(user);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{username}")
	public ResponseEntity<Void> delete(@PathVariable String username) {
		try {
			service.deleteUser(username);
			AuditLogger.log("USER_DELETE", getCurrentUser(), "SUCCESS", "User deleted: " + username);
			return ResponseEntity.noContent().build();
			
		} catch (Exception e) {
			AuditLogger.log("USER_DELETE", getCurrentUser(), "FAILED", "Error deleting user: " + e.getMessage());
			throw new RuntimeException("Error deleting user", e);
		}
	}
	
	@Override
	@PutMapping("/change-password")
	public ResponseEntity<Void> changePassword(
		@AuthenticationPrincipal User authenticatedUser,
		@Valid @RequestBody ChangePasswordRequestDTO dto
	) {
		try {
			service.changePassword(authenticatedUser.getUsername(), dto);
			AuditLogger.log("PASSWORD_CHANGE", authenticatedUser.getUsername(), "SUCCESS", "Password changed successfully");
			return ResponseEntity.noContent().build();
			
		} catch (Exception e) {
			AuditLogger.log("PASSWORD_CHANGE", authenticatedUser.getUsername(), "FAILED", "Error changing password: " + e.getMessage());
			throw new RuntimeException("Error changing password", e);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{username}/roles")
	public ResponseEntity<List<String>> getUserRoles(@PathVariable String username) {
		List<String> roles = service.getUserRoles(username);
		return ResponseEntity.ok(roles);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{username}/roles/assign")
	public ResponseEntity<UserResponseDTO> assignRoles(
		@PathVariable String username,
		@Valid @RequestBody List<String> roleNames
	) {
		try {
			UserResponseDTO updatedUser = service.assignRoles(username, roleNames);
			AuditLogger.log("ROLE_ASSIGN", getCurrentUser(), "SUCCESS", "Role " + roleNames + " assigned to user: " + username);
			return ResponseEntity.ok(updatedUser);
			
		} catch (Exception e) {
			AuditLogger.log("ROLE_ASSIGN", getCurrentUser(), "FAILED", "Error assigning roles to " + username + ": " + e.getMessage());
			throw new RuntimeException("Error assigning roles", e);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{username}/roles/remove")
	public ResponseEntity<UserResponseDTO> removeRoles(
		@PathVariable String username,
		@Valid @RequestBody List<String> roleNames
	) {
		try {
			UserResponseDTO updatedUser = service.removeRoles(username, roleNames);
			AuditLogger.log("ROLE_REMOVE", getCurrentUser(), "SUCCESS", "Role " + roleNames + " removed from user: " + username);
			return ResponseEntity.ok(updatedUser);
			
		} catch (Exception e) {
			AuditLogger.log("ROLE_REMOVE", getCurrentUser(), "FAILED", "Error removing roles from " + username + ": " + e.getMessage());
			throw new RuntimeException("Error removing roles", e);
		}
	}
}