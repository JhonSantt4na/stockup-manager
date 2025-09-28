package com.stockup.StockUp.Manager.contorller;

import com.stockup.StockUp.Manager.dto.ChangePasswordRequestDTO;
import com.stockup.StockUp.Manager.dto.security.request.RegisterRequestDTO;
import com.stockup.StockUp.Manager.dto.security.response.UserResponseDTO;
import com.stockup.StockUp.Manager.model.User;
import com.stockup.StockUp.Manager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService service;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/register",
		produces = {MediaType.APPLICATION_JSON_VALUE},
		consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO credentials) {
		UserResponseDTO response = service.registerUser(credentials);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/update",
		produces = {MediaType.APPLICATION_JSON_VALUE},
		consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<UserResponseDTO> updated(@Valid @RequestBody RegisterRequestDTO credentials) {
		UserResponseDTO response = service.updatedUser(credentials);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/finded/{username}")
	public ResponseEntity<UserResponseDTO> findByUsername(@Valid @PathVariable String username) {
		UserResponseDTO user = service.findUser(username);
		return ResponseEntity.ok(user);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{username}")
	public ResponseEntity<UserResponseDTO> delete(@Valid @PathVariable String username) {
		service.deleteUser(username);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/change-password")
	public ResponseEntity<Void> changePassword(
		@AuthenticationPrincipal User authenticatedUser,
		@Valid @RequestBody ChangePasswordRequestDTO dto
	) {
		service.changePassword(authenticatedUser.getUsername(), dto);
		return ResponseEntity.noContent().build();
	}
}