package com.stockup.StockUp.Manager.service;

import com.stockup.StockUp.Manager.StartUp;
import com.stockup.StockUp.Manager.dto.securityDto.AccountCredentialsDTO;
import com.stockup.StockUp.Manager.dto.securityDto.AccountRegisterDTO;
import com.stockup.StockUp.Manager.dto.securityDto.TokenDTO;
import com.stockup.StockUp.Manager.entity.User;
import com.stockup.StockUp.Manager.entity.security.Permission;
import com.stockup.StockUp.Manager.repository.PermissionsRepository;
import com.stockup.StockUp.Manager.repository.UserRepository;
import com.stockup.StockUp.Manager.security.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {
	
	private static final Logger logger = LoggerFactory.getLogger(StartUp.class);
	private static final Logger auditLogger = LoggerFactory.getLogger("audit");
	
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider tokenProvider;
	private final UserRepository userRepository;
	private final PermissionsRepository repositoryPermission;
	private final PasswordEncoder passwordEncoder;
	
	public ResponseEntity<TokenDTO> login(AccountCredentialsDTO credentials) {
		
		logger.info("Authentication requested for user [{}]", credentials.getUsername());
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				credentials.getUsername(),
				credentials.getPassword()
			)
		);
		
		var user = userRepository.findByUsername(credentials.getUsername());
		if (user == null) {
			throw new UsernameNotFoundException("Username " + credentials.getUsername() + " not found!");
		}
		
		var token = tokenProvider.createAccessToken(
			credentials.getUsername(),
			user.getRoles()
		);
		logger.info("User [{}] authenticated successfully", credentials.getUsername());
		auditLogger.info("User [{}] logged in", credentials.getUsername());
		return ResponseEntity.ok(token);
	}
	
	public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken) {
		logger.info("Refresh token requested for user [{}]", username);
		var user = userRepository.findByUsername(username);
		TokenDTO token;
		if (user != null) {
			token = tokenProvider.refreshToken(refreshToken);
		} else {
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}
		logger.info("New access token generated for user [{}]", username);
		auditLogger.info("Refresh token granted for user [{}]", username);
		return ResponseEntity.ok(token);
	}
	
	public User register(AccountRegisterDTO credentials){
		if (userRepository.existsByUsername(credentials.getUsername())){
			throw new RuntimeException("Username Já cadastrado!");
		}
		String encodedPassword = passwordEncoder.encode(credentials.getPassword());
		
		User user = new User();
		user.setUsername(credentials.getUsername());
		user.setEmail(credentials.getEmail());
		user.setPassword(encodedPassword);
		
		User savedUser = userRepository.save(user);
		
		return savedUser;
	}
	
	private String generateHashedPassword(String password) {
		PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
			"", 8, 185000,
			Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
		
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("pbkdf2", pbkdf2Encoder);
		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
		
		passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
		return passwordEncoder.encode(password);
	}
	
	private Permission createPermission(String description) {
		Permission newPermission = new Permission();
		newPermission.setDescription(description);
		return repositoryPermission.save(newPermission);
	}
}