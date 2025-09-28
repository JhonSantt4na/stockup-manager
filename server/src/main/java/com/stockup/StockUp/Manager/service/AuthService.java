package com.stockup.StockUp.Manager.service;

import com.stockup.StockUp.Manager.StartUp;
import com.stockup.StockUp.Manager.dto.security.response.TokenDTO;
import com.stockup.StockUp.Manager.dto.security.request.LoginRequestDTO;
import com.stockup.StockUp.Manager.model.User;
import com.stockup.StockUp.Manager.repository.UserRepository;
import com.stockup.StockUp.Manager.security.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
	
	private static final Logger logger = LoggerFactory.getLogger(StartUp.class);
	private static final Logger auditLogger = LoggerFactory.getLogger("audit");
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider tokenProvider;
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	public ResponseEntity<TokenDTO> login(LoginRequestDTO credentials) {
		
		logger.info("Authentication requested for user [{}]", credentials.getUsername());
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				credentials.getUsername(),
				credentials.getPassword()
			)
		);
		
		User user = userRepository.findByUsername(credentials.getUsername())
			.orElseThrow(() -> new UsernameNotFoundException("Username " + credentials.getUsername() + " not found!"));
		
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
		if (user.isPresent()) {
			token = tokenProvider.refreshToken(refreshToken);
		} else {
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}
		logger.info("New access token generated for user [{}]", username);
		auditLogger.info("Refresh token granted for user [{}]", username);
		return ResponseEntity.ok(token);
	}
	
}