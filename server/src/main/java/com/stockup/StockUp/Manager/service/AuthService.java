package com.stockup.StockUp.Manager.service;

import com.stockup.StockUp.Manager.dto.security.response.TokenDTO;
import com.stockup.StockUp.Manager.dto.security.request.LoginRequestDTO;
import com.stockup.StockUp.Manager.model.User;
import com.stockup.StockUp.Manager.repository.UserRepository;
import com.stockup.StockUp.Manager.security.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider tokenProvider;
	private final UserRepository userRepository;
	
	public ResponseEntity<TokenDTO> login(LoginRequestDTO credentials) {
		
		logger.info("Authentication requested for user [{}]", credentials.getUsername());
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				credentials.getUsername(),
				credentials.getPassword()
			)
		);
		
		logger.debug("Fetching user details for [{}]", credentials.getUsername());
		User user = userRepository.findByUsername(credentials.getUsername())
			.orElseThrow(() -> {
				logger.warn("User [{}] not found in database after successful authentication", credentials.getUsername());
				return new UsernameNotFoundException("Username " + credentials.getUsername() + " not found!");
			});
		
		logger.debug("Generating token for user [{}]", credentials.getUsername());
		TokenDTO token = tokenProvider.createAccessToken(
			credentials.getUsername(),
			user.getRoles()
		);
		
		logger.info("User [{}] authenticated successfully", credentials.getUsername());
		return ResponseEntity.ok(token);
	}
	
	public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken) {
		logger.info("Refresh token requested for user [{}]", username);
		
		logger.debug("Validating refresh token for user [{}]", username);
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> {
				logger.warn("Refresh token requested for non-existent user [{}]", username);
				return new UsernameNotFoundException("Username " + username + " not found!");
			});
		
		String tokenUsername = tokenProvider.getUsernameFromToken(refreshToken);
		if (!tokenUsername.equals(username)) {
			logger.warn("Token username [{}] does not match requested username [{}]", tokenUsername, username);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		logger.debug("Refreshing token for user [{}]", username);
		TokenDTO token = tokenProvider.refreshToken(refreshToken);
		
		logger.info("New access token generated for user [{}]", username);
		return ResponseEntity.ok(token);
	}
}