package com.stockup.StockUp.Manager.controller.auth;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.dto.Auth.user.request.ChangePasswordRequestDTO;
import com.stockup.StockUp.Manager.dto.Auth.user.request.RegisterUserRequestDTO;
import com.stockup.StockUp.Manager.dto.Auth.user.response.RegistrationResponseDTO;
import com.stockup.StockUp.Manager.dto.Auth.user.request.UpdateUserRequestDTO;
import com.stockup.StockUp.Manager.dto.Auth.user.response.UserResponseDTO;
import com.stockup.StockUp.Manager.exception.CannotDeleteActiveUserException;
import com.stockup.StockUp.Manager.service.auth.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
	
	private final IUserService service;
	
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
	
	@PutMapping("/updateAddress")
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
			
		} catch (CannotDeleteActiveUserException e) {
			AuditLogger.log("USER_DELETE", getCurrentUser(), "FAILED", "Cannot delete active user: " + e.getMessage());
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
			
		} catch (Exception e) {
			AuditLogger.log("USER_DELETE", getCurrentUser(), "FAILED", "Error deleting user: " + e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting user", e);
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/roles/all")
	public ResponseEntity<List<String>> getAllRoles() {
		try {
			List<String> allRoles = service.getAllSystemRoles();
			
			AuditLogger.log("GET_ALL_ROLES", getCurrentUser(), "SUCCESS",
				"Retrieved all system roles: " + allRoles.size() + " roles");
			return ResponseEntity.ok(allRoles);
			
		} catch (Exception e) {
			AuditLogger.log("GET_ALL_ROLES", getCurrentUser(), "FAILED",
				"Error: " + e.getMessage());
			throw new RuntimeException("Error fetching all roles", e);
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updateAddress/{username}")
	public ResponseEntity<UserResponseDTO> updateUserAsAdmin(
		@PathVariable String username,
		@Valid @RequestBody UpdateUserRequestDTO dto
	) {
		try {
			UserResponseDTO response = service.updateUserAsAdmin(username, dto);
			AuditLogger.log("USER_UPDATE_ADMIN", getCurrentUser(), "SUCCESS", "User updated by admin: " + username);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			AuditLogger.log("USER_UPDATE_ADMIN", getCurrentUser(), "FAILED", "Error: " + e.getMessage());
			throw new RuntimeException("Error updating user as admin", e);
		}
	}
	
	@GetMapping("/me")
	public ResponseEntity<UserResponseDTO> getProfile(Authentication authentication) {
		String username = (authentication != null) ? authentication.getName() : "UNKNOWN_USER";
		
		try {
			UserResponseDTO profile = service.findUser(username);
			AuditLogger.log("USER_PROFILE", username, "SUCCESS", "Profile retrieved successfully");
			return ResponseEntity.ok(profile);
			
		} catch (UsernameNotFoundException ex) {
			AuditLogger.log("USER_PROFILE", username, "FAILED", "User not found: " + ex.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado", ex);
			
		} catch (Exception e) {
			AuditLogger.log("USER_PROFILE", username, "FAILED", "Error retrieving profile: " + e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao recuperar o perfil do usuário", e);
		}
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<Page<UserResponseDTO>> listUsers(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String search,
		@RequestParam(defaultValue = "all") String filter
	) {
		Page<UserResponseDTO> usersPage = service.listUsersWithFilter(page, size, search, filter);
		return ResponseEntity.ok(usersPage);
	}
	
	@GetMapping("/list")
	public Page<UserResponseDTO> listUsers(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String search,
		@RequestParam(required = false) Boolean enabled,
		@RequestParam(defaultValue = "username,asc") String[] sort
	) {
		return service.listUsers(page, size, search, enabled, sort);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{username}/toggle")
	public ResponseEntity<UserResponseDTO> toggleUser(@PathVariable String username) {
		try {
			UserResponseDTO updated = service.toggleUserStatus(username);
			AuditLogger.log("USER_TOGGLE", getCurrentUser(), "SUCCESS",
				"User " + username + " toggled active/inactive");
			return ResponseEntity.ok(updated);
		} catch (Exception e) {
			AuditLogger.log("USER_TOGGLE", getCurrentUser(), "FAILED", "Error: " + e.getMessage());
			throw new RuntimeException("Error toggling user status", e);
		}
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