package com.stockup.StockUp.Manager.contorller;

import com.stockup.StockUp.Manager.dto.security.request.LoginRequestDTO;
import com.stockup.StockUp.Manager.dto.security.request.RegisterRequestDTO;
import com.stockup.StockUp.Manager.exception.InvalidCredentialsException;
import com.stockup.StockUp.Manager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService service;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO credentials) {
		var token = service.login(credentials);
		if (token == null) throw new InvalidCredentialsException();
		return ResponseEntity.ok(token);
	}
	
	@PutMapping("/refresh/{username}")
	public ResponseEntity<?> refreshToken(
		@PathVariable("username") String username,
		@RequestHeader("Authorization") String refreshToken) {
		
		if (username == null || username.isBlank() || refreshToken == null || refreshToken.isBlank()) {
			throw new InvalidCredentialsException();
		}
		
		var token = service.refreshToken(username, refreshToken);
		if (token == null) throw new InvalidCredentialsException();
		return ResponseEntity.ok(token);
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO credentials) {
		var user = service.register(credentials);
		return ResponseEntity.status(201).body(user);
	}
}