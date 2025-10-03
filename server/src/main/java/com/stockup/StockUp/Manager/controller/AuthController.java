package com.stockup.StockUp.Manager.controller;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.Docs.AuthControllerDocs;
import com.stockup.StockUp.Manager.dto.security.request.LoginRequestDTO;
import com.stockup.StockUp.Manager.dto.security.response.TokenDTO;
import com.stockup.StockUp.Manager.exception.InvalidCredentialsException;
import com.stockup.StockUp.Manager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {
	
	private final AuthService service;
	
	@Override
	@PostMapping("/login")
	public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginRequestDTO credentials) {
		try {
			var token = service.login(credentials);
			AuditLogger.log("LOGIN", credentials.getUsername(), "SUCCESS", "Authenticated successfully");
			return ResponseEntity.ok(token);
			
		} catch (BadCredentialsException e) {
			AuditLogger.log("LOGIN", credentials.getUsername(), "FAILED", "Invalid credentials");
			throw e;
			
		} catch (UsernameNotFoundException e) {
			AuditLogger.log("LOGIN", credentials.getUsername(), "FAILED", "User not found");
			throw e;
			
		} catch (Exception e) {
			AuditLogger.log("LOGIN", credentials.getUsername(), "FAILED", "Internal error: " + e.getMessage());
			throw new RuntimeException("Server error", e);
		}
	}
	
	@Override
	@PutMapping("/refresh/{username}")
	public ResponseEntity<TokenDTO> refreshToken(
		@PathVariable("username") String username,
		@RequestHeader("Authorization") String refreshToken) {
		try {
			if (username == null || username.isBlank() || refreshToken == null || refreshToken.isBlank()) {
				AuditLogger.log("REFRESH_TOKEN", "unknown", "FAILED", "Incomplete data");
				throw new InvalidCredentialsException();
			}
			var token = service.refreshToken(username, refreshToken);
			
			AuditLogger.log("REFRESH_TOKEN", username, "SUCCESS", "Token renewed");
			return ResponseEntity.ok(token);
		
		} catch (Exception e) {
			AuditLogger.log("REFRESH_TOKEN", username != null ? username : "unknown", "FAILED", "Error: " + e.getMessage());
			throw new RuntimeException("Server error", e);
		}
	}
}