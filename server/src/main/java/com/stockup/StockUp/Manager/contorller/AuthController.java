package com.stockup.StockUp.Manager.contorller;

import com.stockup.StockUp.Manager.dto.securityDto.AccountCredentialsDTO;
import com.stockup.StockUp.Manager.service.AuthService;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	@Autowired
	private final AuthService service;
	
	@GetMapping("/login")
	public String showLoginPage() {
		return "login";
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> signin(@Valid @RequestBody AccountCredentialsDTO credentials) {
		
		if (credentialsIsInvalid(credentials))return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		var token = service.signIn(credentials);
		
		if (token == null) ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		return  ResponseEntity.ok().body(token);
	}
	
	@PutMapping("/refresh/{username}")
	public ResponseEntity<?> refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
		
		if (parametersAreInvalid(username, refreshToken)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		var token = service.refreshToken(username, refreshToken);
		if (token == null) ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		return  ResponseEntity.ok().body(token);
	}
	
	private boolean parametersAreInvalid(String username, String refreshToken) {
		return StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken);
	}
	
	private static boolean credentialsIsInvalid(AccountCredentialsDTO credentials) {
		
		return credentials == null ||
			StringUtils.isBlank(credentials.getPassword()) ||
			StringUtils.isBlank(credentials.getUserName());
	}
}