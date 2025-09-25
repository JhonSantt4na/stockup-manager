package com.stockup.StockUp.Manager.contorller;

import com.stockup.StockUp.Manager.dto.securityDto.AccountCredentialsDTO;
import com.stockup.StockUp.Manager.dto.securityDto.AccountRegisterDTO;
import com.stockup.StockUp.Manager.dto.securityDto.RegisterDTO;
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
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody AccountCredentialsDTO credentials) {
		
		if (credentialsIsInvalid(credentials))return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		var token = service.login(credentials);
		
		if (token == null) ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		return  ResponseEntity.ok().body(token);
	}
	
	@PutMapping("/refresh/{username}")
	public ResponseEntity<?> refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
		
		if (parametersAreInvalid(username, refreshToken)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		var token = service.refreshToken(username, refreshToken);
		if (token == null)
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		else {
			return  ResponseEntity.ok().body(token);
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody AccountRegisterDTO credentials){
		
		if (registerIsInvalid(credentials)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid client request!");
		}
		
		var user = service.register(credentials);
		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}
	
	private boolean registerIsInvalid(AccountRegisterDTO credentials){
		return credentials == null ||
			StringUtils.isBlank(credentials.getUsername()) ||
			StringUtils.isBlank(credentials.getPassword()) ||
			StringUtils.isBlank(credentials.getEmail());
	}
	
	private boolean parametersAreInvalid(String username, String refreshToken) {
		return StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken);
	}
	
	private static boolean credentialsIsInvalid(AccountCredentialsDTO credentials) {
		return credentials == null ||
			StringUtils.isBlank(credentials.getPassword()) ||
			StringUtils.isBlank(credentials.getUsername());
	}
}