package com.stockup.StockUp.Manager.controller;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.dto.user.request.ChangePasswordRequestDTO;
import com.stockup.StockUp.Manager.dto.user.request.RegisterUserRequestDTO;
import com.stockup.StockUp.Manager.dto.user.response.RegistrationResponseDTO;
import com.stockup.StockUp.Manager.dto.user.request.UpdateUserRequestDTO;
import com.stockup.StockUp.Manager.dto.user.response.UserResponseDTO;
import com.stockup.StockUp.Manager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	
	private final UserService service;
	
	@PostMapping("/register")
	public ResponseEntity<RegistrationResponseDTO> register(@Valid @RequestBody RegisterUserRequestDTO credentials) {
		try {
			RegistrationResponseDTO response = service.registerUser(credentials);
			String currentUser = getCurrentUser() != null ? getCurrentUser() : "ANONYMOUS";
			AuditLogger.log("USER_REGISTER", currentUser, "SUCCESS", "User registered: " + credentials.getUsername() + " with auto-token");
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
			
		} catch (Exception e) {
			String currentUser = getCurrentUser() != null ? getCurrentUser() : "ANONYMOUS";
			AuditLogger.log("USER_REGISTER", currentUser, "FAILED", "Error: " + e.getMessage());
			throw new RuntimeException("Error registering user", e);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<UserResponseDTO> updated(
		Authentication authentication,
		@Valid @RequestBody UpdateUserRequestDTO dto
	) {
		String username = authentication.getName();
		
		try {
			UserResponseDTO response = service.updatedUser(username, dto);
			AuditLogger.log("USER_UPDATE", username, "SUCCESS", "User updated: " + username);
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			AuditLogger.log("USER_UPDATE", username, "FAILED", "Error: " + e.getMessage());
			throw new RuntimeException("Error updating user", e);
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/find/{username}")
	public ResponseEntity<UserResponseDTO> findByUsername(@PathVariable String username) {
		UserResponseDTO user = service.findUser(username);
		return ResponseEntity.ok(user);
	}
	
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
	
	@GetMapping("/me")
	public ResponseEntity<UserResponseDTO> getProfile(Authentication authentication) {
		try {
			String username = authentication.getName();
			UserResponseDTO profile = service.findUser(username);
			AuditLogger.log("USER_PROFILE", username, "SUCCESS", "Profile retrieved successfully");
			return ResponseEntity.ok(profile);
			
		} catch (Exception e) {
			String username = authentication.getName();
			AuditLogger.log("USER_PROFILE", username, "FAILED", "Error retrieving profile: " + e.getMessage());
			throw new RuntimeException("Error retrieving profile", e);
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<Page<UserResponseDTO>> listUsers(
		@RequestParam(required = false) String role,
		Pageable pageable
	) {
		Page<UserResponseDTO> usersPage = service.listUsers(role, pageable);
		return ResponseEntity.ok(usersPage);
	}
	
	@PutMapping("/change-password")
	public ResponseEntity<Void> changePassword(
		Authentication authentication,
		@Valid @RequestBody ChangePasswordRequestDTO dto
	) {
		String username = authentication.getName();
		try {
			service.changePassword(username, dto);
			AuditLogger.log("PASSWORD_CHANGE", username, "SUCCESS", "Password changed successfully");
			return ResponseEntity.noContent().build();
			
		} catch (Exception e) {
			AuditLogger.log("PASSWORD_CHANGE", username, "FAILED", "Error changing password: " + e.getMessage());
			throw new RuntimeException("Error changing password", e);
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{username}/roles")
	public ResponseEntity<List<String>> getUserRoles(@PathVariable String username) {
		List<String> roles = service.getUserRoles(username);
		return ResponseEntity.ok(roles);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{username}/roles/assign")
	public ResponseEntity<UserResponseDTO> assignRoles(
		@PathVariable String username,
		@Valid @RequestBody List<String> roleNames
	) {
		try {
			UserResponseDTO updatedUser = service.assignRoles(username, roleNames);
			AuditLogger.log("ROLE_ASSIGN", getCurrentUser(), "SUCCESS", "Roles " + roleNames + " assigned to user: " + username);
			return ResponseEntity.ok(updatedUser);
			
		} catch (Exception e) {
			AuditLogger.log("ROLE_ASSIGN", getCurrentUser(), "FAILED", "Error assigning roles to " + username + ": " + e.getMessage());
			throw new RuntimeException("Error assigning roles", e);
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{username}/roles/remove")
	public ResponseEntity<UserResponseDTO> removeRoles(
		@PathVariable String username,
		@Valid @RequestBody List<String> roleNames
	) {
		try {
			UserResponseDTO updatedUser = service.removeRoles(username, roleNames);
			AuditLogger.log("ROLE_REMOVE", getCurrentUser(), "SUCCESS", "Roles " + roleNames + " removed from user: " + username);
			return ResponseEntity.ok(updatedUser);
			
		} catch (Exception e) {
			AuditLogger.log("ROLE_REMOVE", getCurrentUser(), "FAILED", "Error removing roles from " + username + ": " + e.getMessage());
			throw new RuntimeException("Error removing roles", e);
		}
	}
}