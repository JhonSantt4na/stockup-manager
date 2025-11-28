package com.stockup.StockUp.Manager.service;

import com.stockup.StockUp.Manager.dto.Auth.user.request.ChangePasswordRequestDTO;
import com.stockup.StockUp.Manager.dto.Auth.user.request.RegisterUserRequestDTO;
import com.stockup.StockUp.Manager.dto.Auth.user.response.UserResponseDTO;
import com.stockup.StockUp.Manager.exception.InvalidCredentialsException;
import com.stockup.StockUp.Manager.exception.UsernameAlreadyExistsException;
import com.stockup.StockUp.Manager.mapper.user.UserMapper;
import com.stockup.StockUp.Manager.model.user.User;
import com.stockup.StockUp.Manager.model.user.Role;
import com.stockup.StockUp.Manager.repository.user.RoleRepository;
import com.stockup.StockUp.Manager.repository.user.UserRepository;
import com.stockup.StockUp.Manager.service.auth.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private RoleRepository roleRepository;
	
	@Mock
	private UserMapper mapper;
	
	@InjectMocks
	private UserService userService;
	
	private User testUser;
	private RegisterUserRequestDTO registerRequest;
	private ChangePasswordRequestDTO changePasswordRequest;
	private List<String> roleNames;
	private Role testRole;
	private UUID testUuid;
	
	@BeforeEach
	void setUp() {
		testUuid = UUID.randomUUID();
		testUser = new User();
		testUser.setId(testUuid);
		testUser.setUsername("testuser");
		testUser.setFullName("Test User");
		testUser.setEmail("test@example.com");
		testUser.setPassword("encodedPassword");
		testUser.setCreatedAt(LocalDateTime.now());
		testUser.setRoles(new ArrayList<>());
		
		registerRequest = new RegisterUserRequestDTO();
		registerRequest.setUsername("testuser");
		registerRequest.setFullName("Test User");
		registerRequest.setEmail("test@example.com");
		registerRequest.setPassword("password123");
		
		changePasswordRequest = new ChangePasswordRequestDTO();
		changePasswordRequest.setCurrentPassword("password123");
		changePasswordRequest.setNewPassword("newPassword456");
		
		roleNames = Arrays.asList("ROLE_ADMIN", "ROLE_USER");
		testRole = new Role();
		testRole.setName("ROLE_ADMIN");
	}
	
	@Test
	void loadUserByUsername_WhenUserExists_ShouldReturnUser() {
		// Arrange
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
		
		// Act
		UserDetails result = userService.loadUserByUsername("testuser");
		
		// Assert
		assertEquals(testUser, result);
		verify(userRepository, times(1)).findByUsername("testuser");
	}
	
	@Test
	void loadUserByUsername_WhenUserNotExists_ShouldThrowUsernameNotFoundException() {
		// Arrange
		when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
		
		// Act & Assert
		UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
			() -> userService.loadUserByUsername("nonexistent"));
		assertEquals("Username 'nonexistent' not found!", exception.getMessage());
		verify(userRepository, times(1)).findByUsername("nonexistent");
	}
	
	@Test
	void assignRoles_WhenUserAndRolesExist_ShouldAssignAndReturnUpdatedUserDTO() {
		// Arrange
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
		when(roleRepository.findAllByNameIn(roleNames)).thenReturn(Collections.singletonList(testRole));
		when(userRepository.save(testUser)).thenReturn(testUser);
		UserResponseDTO expectedDTO = new UserResponseDTO(
			testUuid,
			"testuser",
			"Test User",
			"test@example.com",
			List.of("ROLE_ADMIN")
		);
		when(mapper.entityToResponse(testUser)).thenReturn(expectedDTO);
		
		// Act
		UserResponseDTO result = userService.assignRoles("testuser", roleNames);
		
		// Assert
		assertEquals(expectedDTO, result);
		assertEquals(1, testUser.getRoles().size());
		verify(userRepository, times(1)).save(testUser);
		verify(roleRepository, times(1)).findAllByNameIn(roleNames);
	}
	
	@Test
	void assignRoles_WhenNoValidRoles_ShouldThrowIllegalArgumentException() {
		// Arrange
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
		when(roleRepository.findAllByNameIn(roleNames)).thenReturn(new ArrayList<>());
		
		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> userService.assignRoles("testuser", roleNames));
		assertEquals("No valid roles found to assign", exception.getMessage());
	}
	
	@Test
	void removeRoles_WhenUserAndRolesExist_ShouldRemoveAndReturnUpdatedUserDTO() {
		// Arrange
		testUser.getRoles().add(testRole);
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
		when(userRepository.save(testUser)).thenReturn(testUser);
		UserResponseDTO expectedDTO = new UserResponseDTO(
			testUuid,
			"testuser",
			"Test User",
			"test@example.com",
			List.of()
		);
		when(mapper.entityToResponse(testUser)).thenReturn(expectedDTO);
		
		// Act
		UserResponseDTO result = userService.removeRoles("testuser", roleNames);
		
		// Assert
		assertEquals(expectedDTO, result);
		assertTrue(testUser.getRoles().isEmpty());
		verify(userRepository, times(1)).save(testUser);
	}
	
	@Test
	void getUserRoles_WhenUserExists_ShouldReturnRoleAuthorities() {
		// Arrange
		testUser.getRoles().add(testRole);
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
		
		// Act
		List<String> result = userService.getUserRoles("testuser");
		
		// Assert
		assertEquals(1, result.size());
		assertEquals("ROLE_ADMIN", result.getFirst());
		verify(userRepository, times(1)).findByUsername("testuser");
	}
	
//	@Test
//	void registerUser_WhenUsernameNotExists_ShouldRegisterAndReturnDTO() {
//		// Arrange
//		when(userRepository.existsByUsername("testuser")).thenReturn(false);
//		when(mapper.registerToUser(registerRequest)).thenReturn(testUser);
//		when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
//		when(userRepository.save(testUser)).thenReturn(testUser);
//		UserResponseDTO expectedDTO = new UserResponseDTO(
//			testUuid,
//			"testuser",
//			"Test User",
//			"test@example.com",
//			List.of()
//		);
//		when(mapper.entityToResponse(testUser)).thenReturn(expectedDTO);
//
//		// Act
//		UserResponseDTO result = userService.registerUser(registerRequest);
//
//		// Assert
//		assertEquals(expectedDTO, result);
//		verify(passwordEncoder, times(1)).encode("password123");
//		verify(userRepository, times(1)).save(testUser);
//	}
	
	@Test
	void registerUser_WhenUsernameExists_ShouldThrowUsernameAlreadyExistsException() {
		// Arrange
		when(userRepository.existsByUsername("testuser")).thenReturn(true);
		
		// Act & Assert
		UsernameAlreadyExistsException exception = assertThrows(UsernameAlreadyExistsException.class,
			() -> userService.registerUser(registerRequest));
		assertTrue(exception.getMessage().contains("já está em uso"));
	}
	
//	@Test
//	void updatedUser_WhenValidFieldsProvided_ShouldUpdateAndReturnDTO() {
//		// Arrange
//		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
//		when(userRepository.save(testUser)).thenReturn(testUser);
//		UserResponseDTO expectedDTO = new UserResponseDTO(
//			testUuid,
//			"testuser",
//			"Updated Name",
//			"updated@example.com",
//			List.of()
//		);
//		when(mapper.entityToResponse(testUser)).thenReturn(expectedDTO);
//
//		RegisterUserRequestDTO updateRequest = new RegisterUserRequestDTO();
//		updateRequest.setUsername("testuser");
//		updateRequest.setFullName("Updated Name");
//		updateRequest.setEmail("updated@example.com");
//		updateRequest.setPassword("newpass");
//		when(passwordEncoder.encode("newpass")).thenReturn("newEncodedPass");
//
//		// Act
//		UserResponseDTO result = userService.updatedUser(updateRequest);
//
//		// Assert
//		assertEquals(expectedDTO, result);
//		assertEquals("Updated Name", testUser.getFullName());
//		assertEquals("updated@example.com", testUser.getEmail());
//		verify(passwordEncoder, times(1)).encode("newpass");
//		verify(userRepository, times(1)).save(testUser);
//	}
	
//	@Test
//	void updatedUser_WhenNoFieldsProvided_ShouldReturnExistingDTO() {
//		// Arrange
//		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
//		UserResponseDTO expectedDTO = new UserResponseDTO(
//			testUuid,
//			"testuser",
//			"Test User",
//			"test@example.com",
//			List.of()
//		);
//		when(mapper.entityToResponse(testUser)).thenReturn(expectedDTO);
//
//		RegisterUserRequestDTO noChangeRequest = new RegisterUserRequestDTO();
//		noChangeRequest.setUsername("testuser");
//
//		// Act
//		UserResponseDTO result = userService.updatedUser(noChangeRequest);
//
//		// Assert
//		assertEquals(expectedDTO, result);
//		verify(userRepository, never()).save(any(User.class));
//	}
	
	@Test
	void findUser_WhenUserExists_ShouldReturnDTO() {
		// Arrange
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
		UserResponseDTO expectedDTO = new UserResponseDTO(
			testUuid,
			"testuser",
			"Test User",
			"test@example.com",
			List.of()
		);
		when(mapper.entityToResponse(testUser)).thenReturn(expectedDTO);
		
		// Act
		UserResponseDTO result = userService.findUser("testuser");
		
		// Assert
		assertEquals(expectedDTO, result);
		verify(userRepository, times(1)).findByUsername("testuser");
	}
	
	@Test
	void findUser_WhenUserNotExists_ShouldThrowUsernameNotFoundException() {
		// Arrange
		when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
		
		// Act & Assert
		assertThrows(UsernameNotFoundException.class, () -> userService.findUser("nonexistent"));
		verify(userRepository, times(1)).findByUsername("nonexistent");
	}
	
	@Test
	void deleteUser_WhenUserExists_ShouldSoftDelete() {
		// Arrange
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
		when(userRepository.save(testUser)).thenReturn(testUser);
		
		// Act
		userService.deleteUser("testuser");
		
		// Assert
		assertNotNull(testUser.getDeletedAt());
		verify(userRepository, times(1)).save(testUser);
	}
	
	@Test
	void listUsers_WhenNoRoleFilter_ShouldReturnPagedDTOs() {
		// Arrange
		Pageable pageable = PageRequest.of(0, 10);
		Page<User> userPage = new PageImpl<>(Collections.singletonList(testUser));
		when(userRepository.findAll(pageable)).thenReturn(userPage);
		UserResponseDTO dto = new UserResponseDTO(
			testUuid,
			"testuser",
			"Test User",
			"test@example.com",
			List.of()
		);
		when(mapper.entityToResponse(testUser)).thenReturn(dto);
		
		// Act
		Page<UserResponseDTO> result = userService.listUsers(null, pageable);
		
		// Assert
		assertEquals(1, result.getNumberOfElements());
		assertEquals(dto,
			result.getContent().getFirst());
		verify(userRepository, times(1)).findAll(pageable);
	}
	
	@Test
	void listUsers_WhenRoleFilter_ShouldReturnFilteredPagedDTOs() {
		// Arrange
		Pageable pageable = PageRequest.of(0, 10);
		Page<User> userPage = new PageImpl<>(Collections.singletonList(testUser));
		when(userRepository.findAllByRoles_Name("ROLE_ADMIN", pageable)).thenReturn(userPage);
		UserResponseDTO dto = new UserResponseDTO(
			testUuid,
			"testuser",
			"Test User",
			"test@example.com",
			List.of("ROLE_ADMIN")
		);
		when(mapper.entityToResponse(testUser)).thenReturn(dto);
		
		// Act
		Page<UserResponseDTO> result = userService.listUsers("ROLE_ADMIN", pageable);
		
		// Assert
		assertEquals(1, result.getNumberOfElements());
		assertEquals(dto, result.getContent().getFirst());
		verify(userRepository, times(1)).findAllByRoles_Name("ROLE_ADMIN", pageable);
	}
	
	@Test
	void changePassword_WhenValidCredentials_ShouldUpdatePassword() {
		// Arrange
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
		when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
		when(passwordEncoder.encode("newPassword456")).thenReturn("newEncodedPassword");
		when(userRepository.save(testUser)).thenReturn(testUser);
		
		// Act
		userService.changePassword("testuser", changePasswordRequest);
		
		// Assert
		verify(passwordEncoder, times(1)).matches("password123", "encodedPassword");
		verify(passwordEncoder, times(1)).encode("newPassword456");
		verify(userRepository, times(1)).save(testUser);
	}
	
	@Test
	void changePassword_WhenInvalidCurrentPassword_ShouldThrowInvalidCredentialsException() {
		// Arrange
		ChangePasswordRequestDTO invalidRequest = new ChangePasswordRequestDTO();
		invalidRequest.setCurrentPassword("wrongpass");
		invalidRequest.setNewPassword("newPassword456");
		
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
		when(passwordEncoder.matches("wrongpass", "encodedPassword")).thenReturn(false);
		
		// Act & Assert
		InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class,
			() -> userService.changePassword("testuser", invalidRequest));
		assertEquals("Current password is incorrect", exception.getMessage());
		verify(passwordEncoder, times(1)).matches("wrongpass", "encodedPassword");
		verify(userRepository, never()).save(any(User.class));
	}
}