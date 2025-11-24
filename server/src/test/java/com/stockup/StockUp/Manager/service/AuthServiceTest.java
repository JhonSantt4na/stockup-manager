package com.stockup.StockUp.Manager.service;

import com.stockup.StockUp.Manager.dto.Auth.security.request.LoginRequestDTO;
import com.stockup.StockUp.Manager.dto.Auth.security.response.TokenDTO;
import com.stockup.StockUp.Manager.model.user.User;
import com.stockup.StockUp.Manager.model.user.Role;
import com.stockup.StockUp.Manager.repository.UserRepository;
import com.stockup.StockUp.Manager.security.JwtTokenProvider;
import com.stockup.StockUp.Manager.service.auth.impl.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.access.AccessDeniedException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
	
	@Mock
	private AuthenticationManager authenticationManager;
	
	@Mock
	private JwtTokenProvider tokenProvider;
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private AuthService authService;
	
	private User testUser;
	private LoginRequestDTO loginRequest;
	private TokenDTO expectedToken;
	
	@BeforeEach
	void setUp() {
		UUID testUuid = UUID.randomUUID();
		testUser = new User();
		testUser.setId(testUuid);
		testUser.setUsername("testuser");
		testUser.setFullName("Test User");
		testUser.setEmail("test@example.com");
		testUser.setPassword("encodedPassword");
		testUser.setRoles(List.of(new Role("ROLE_USER")));
		
		loginRequest = new LoginRequestDTO("testuser", "password123");
		
		expectedToken = new TokenDTO(
			"testuser",
			true,
			new Date(),
			new Date(System.currentTimeMillis() + 3600000), // 1h expiration
			"jwtAccessToken",
			"jwtRefreshToken"
		);
	}
	
	@Test
	void login_WhenValidCredentials_ShouldReturnTokenDTO() {
		// Arrange
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null); // Success
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
		when(tokenProvider.createAccessToken(eq("testuser"), anyList())).thenReturn(expectedToken);
		
		// Act
		TokenDTO result = authService.login(loginRequest);
		
		// Assert
		assertEquals(expectedToken, result);
		verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(userRepository, times(1)).findByUsername("testuser");
		verify(tokenProvider, times(1)).createAccessToken(eq("testuser"), anyList());
	}
	
	@Test
	void login_WhenInvalidCredentials_ShouldThrowBadCredentialsException() {
		// Arrange
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
			.thenThrow(new BadCredentialsException("Invalid credentials"));
		
		// Act & Assert
		assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
		verify(userRepository, never()).findByUsername(anyString());
		verify(tokenProvider, never()).createAccessToken(anyString(), anyList());
	}
	
	@Test
	void login_WhenUserNotFound_ShouldThrowUsernameNotFoundException() {
		// Arrange
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
		
		// Act & Assert
		assertThrows(UsernameNotFoundException.class, () -> authService.login(loginRequest));
		verify(tokenProvider, never()).createAccessToken(anyString(), anyList());
	}
	
	@Test
	void refreshToken_WhenValid_ShouldReturnNewTokenDTO() {
		// Arrange
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
		when(tokenProvider.getUsernameFromToken("validRefreshToken")).thenReturn("testuser");
		when(tokenProvider.refreshToken("validRefreshToken")).thenReturn(expectedToken);
		
		// Act
		TokenDTO result = authService.refreshToken("testuser", "validRefreshToken");
		
		// Assert
		assertEquals(expectedToken, result);
		verify(userRepository, times(1)).findByUsername("testuser");
		verify(tokenProvider, times(1)).getUsernameFromToken("validRefreshToken");
		verify(tokenProvider, times(1)).refreshToken("validRefreshToken");
	}
	
	@Test
	void refreshToken_WhenUserNotFound_ShouldThrowUsernameNotFoundException() {
		// Arrange
		when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
		
		// Act & Assert
		assertThrows(UsernameNotFoundException.class, () -> authService.refreshToken("nonexistent", "validRefreshToken"));
		verify(tokenProvider, never()).getUsernameFromToken(anyString());
		verify(tokenProvider, never()).refreshToken(anyString());
	}
	
	@Test
	void refreshToken_WhenUsernameMismatch_ShouldThrowAccessDeniedException() {
		// Arrange
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
		when(tokenProvider.getUsernameFromToken("validRefreshToken")).thenReturn("wronguser");
		
		// Act & Assert
		assertThrows(AccessDeniedException.class, () -> authService.refreshToken("testuser", "validRefreshToken"));
		verify(tokenProvider, never()).refreshToken(anyString());
	}
}