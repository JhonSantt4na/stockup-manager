package com.stockup.StockUp.Manager.controller.auth;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.auth.Docs.AuthControllerDocs;
import com.stockup.StockUp.Manager.dto.Auth.security.request.LoginRequestDTO;
import com.stockup.StockUp.Manager.dto.Auth.security.response.TokenDTO;
import com.stockup.StockUp.Manager.exception.InvalidCredentialsException;
import com.stockup.StockUp.Manager.security.JwtTokenProvider;
import com.stockup.StockUp.Manager.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {
	
	private final AuthService service;
	private final JwtTokenProvider tokenProvider;
	
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
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		String token = tokenProvider.resolveToken(request);
		
		if (token == null || token.isBlank()) {
			AuditLogger.log("LOGOUT", "unknown", "FAILED", "No token provided");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is required");
		}
		
		try {
			String username = tokenProvider.getUsernameFromToken(token);
			AuditLogger.log("LOGOUT", username, "SUCCESS", "Logout successful");
			return ResponseEntity.ok("Logout successful");
		} catch (Exception e) {
			AuditLogger.log("LOGOUT", "unknown", "FAILED", "Invalid token: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}
	}
	
	@Override
	@PutMapping("/refresh/{username}")
	public ResponseEntity<TokenDTO> refreshToken(
		@PathVariable("username") String username,
		@RequestHeader("X-Refresh-Token") String refreshToken) {
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