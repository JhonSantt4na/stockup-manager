package com.stockup.StockUp.Manager.service;

import com.stockup.StockUp.Manager.dto.security.response.TokenDTO;
import com.stockup.StockUp.Manager.dto.security.request.LoginRequestDTO;
import com.stockup.StockUp.Manager.model.User;
import com.stockup.StockUp.Manager.model.security.Role;
import com.stockup.StockUp.Manager.repository.UserRepository;
import com.stockup.StockUp.Manager.security.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider tokenProvider;
	private final UserRepository userRepository;
	
	public TokenDTO login(LoginRequestDTO credentials) {
		logger.info("Authentication requested for user [{}]", credentials.getUsername());
		
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
		);
		
		User user = userRepository.findByUsername(credentials.getUsername())
			.orElseThrow(() -> {
				logger.warn("User [{}] not found after authentication", credentials.getUsername());
				return new UsernameNotFoundException("Username " + credentials.getUsername() + " not found!");
			});
		
		List<String> roleNames = user.getRoles().stream()
			.map(Role::getName)
			.collect(Collectors.toList());
		
		logger.debug("Generating token for user [{}]", credentials.getUsername());
		return tokenProvider.createAccessToken(user.getUsername(), roleNames);
	}
	
	public TokenDTO refreshToken(String username, String refreshToken) {
		logger.info("Refresh token requested for user [{}]", username);
		
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));
		
		String tokenUsername = tokenProvider.getUsernameFromToken(refreshToken);
		if (!tokenUsername.equals(username)) {
			throw new AccessDeniedException("Token username does not match requested username");
		}
		
		return tokenProvider.refreshToken(refreshToken);
	}
}