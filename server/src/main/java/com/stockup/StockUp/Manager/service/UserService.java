package com.stockup.StockUp.Manager.service;

import com.stockup.StockUp.Manager.dto.security.response.TokenDTO;
import com.stockup.StockUp.Manager.dto.user.request.ChangePasswordRequestDTO;
import com.stockup.StockUp.Manager.dto.user.request.RegisterUserRequestDTO;
import com.stockup.StockUp.Manager.dto.user.response.RegistrationResponseDTO;
import com.stockup.StockUp.Manager.dto.user.request.UpdateUserRequestDTO;
import com.stockup.StockUp.Manager.dto.user.response.UserResponseDTO;
import com.stockup.StockUp.Manager.exception.InvalidCredentialsException;
import com.stockup.StockUp.Manager.exception.UsernameAlreadyExistsException;
import com.stockup.StockUp.Manager.mapper.UserMapper;
import com.stockup.StockUp.Manager.model.User;
import com.stockup.StockUp.Manager.model.security.Role;
import com.stockup.StockUp.Manager.repository.RoleRepository;
import com.stockup.StockUp.Manager.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements UserDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UserMapper mapper;
	
	private final AuthService authService;
	
	public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository, UserMapper mapper, @Lazy AuthService authService) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.mapper = mapper;
		this.authService = authService;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("User lookup requested for username [{}]", username);
		
		return userRepository.findByUsername(username)
			.map(user -> {
				logger.info("User [{}] found in the database", username);
				return user;
			})
			.orElseThrow(() -> {
				logger.warn("User [{}] not found", username);
				return new UsernameNotFoundException("Username '" + username + "' not found!");
			});
	}
	
	public UserResponseDTO assignRoles(String username, List<String> roleNames) {
		logger.info("Assigning roles to user [{}]: {}", username, roleNames);
		
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
		
		List<Role> rolesToAssign = roleRepository.findAllByNameIn(roleNames);
		if (rolesToAssign.isEmpty()) {
			logger.warn("No valid roles found to assign for user [{}]", username);
			throw new IllegalArgumentException("No valid roles found to assign");
		}
		
		Set<Role> currentRoles = new HashSet<>(user.getRoles());
		currentRoles.addAll(rolesToAssign);
		user.setRoles(new ArrayList<>(currentRoles));
		
		User updatedUser = userRepository.save(user);
		logger.info("Assigned {} roles to user [{}]", rolesToAssign.size(), username);
		
		return mapper.entityToResponse(updatedUser);
	}
	
	public UserResponseDTO removeRoles(String username, List<String> roleNames) {
		logger.info("Removing roles from user [{}]: {}", username, roleNames);
		
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
		
		user.getRoles().removeIf(role -> roleNames.contains(role.getName()));
		User savedUser = userRepository.save(user);
		
		logger.info("Removed {} roles from user [{}]", roleNames.size(), username);
		return mapper.entityToResponse(savedUser);
	}
	
	public List<String> getUserRoles(String username) {
		logger.debug("Getting roles for user: [{}]", username);
		
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> {
				logger.warn("User not found: [{}]", username);
				return new UsernameNotFoundException("User not found: " + username);
			});
		
		List<String> roles = user.getRoles().stream()
			.map(Role::getAuthority)
			.collect(Collectors.toList());
		
		logger.debug("User [{}] has {} roles", username, roles.size());
		return roles;
	}
	
	public RegistrationResponseDTO registerUser(RegisterUserRequestDTO credentials) {
		logger.info("Registering new user: [{}]", credentials.getUsername());
		
		if (userRepository.existsByUsername(credentials.getUsername())) {
			logger.warn("Attempt to register existing username: [{}]", credentials.getUsername());
			throw new UsernameAlreadyExistsException("Username already exists: " + credentials.getUsername());
		}
		
		if (userRepository.existsByEmail(credentials.getEmail())) {
			logger.warn("Attempt to register duplicate email: [{}]", credentials.getEmail());
			throw new IllegalArgumentException("Email already in use: " + credentials.getEmail());
		}
		
		User newUser = new User();
		newUser.setUsername(credentials.getUsername());
		newUser.setFullName(credentials.getFullName());
		newUser.setEmail(credentials.getEmail());
		newUser.setPassword(passwordEncoder.encode(credentials.getPassword()));
		newUser.setCreatedAt(LocalDateTime.now());
		newUser.setEnabled(true);
		
		Role userRole = roleRepository.findByName("USER")
			.orElseThrow(() -> {
				logger.error("Default USER role not found during registration");
				return new RuntimeException("Default USER role not found");
			});
		newUser.setRoles(new ArrayList<>(List.of(userRole)));
		
		User userSaved = userRepository.save(newUser);
		logger.info("User registered successfully: [{}] with default role USER", credentials.getUsername());
		
		List<String> roleNames = userSaved.getRoles().stream()
			.map(Role::getName)
			.collect(Collectors.toList());
		TokenDTO tokenDto = authService.generateTokenForNewUser(userSaved.getUsername(), roleNames);
		logger.debug("Generated auto-login TokenDTO for new user [{}]", credentials.getUsername());
		
		UserResponseDTO userDto = new UserResponseDTO(
			userSaved.getId(),
			userSaved.getUsername(),
			userSaved.getFullName(),
			userSaved.getEmail(),
			roleNames
		);
		
		return new RegistrationResponseDTO(userDto, tokenDto);
	}
	
	public UserResponseDTO updatedUser(String username, UpdateUserRequestDTO dto) {
		logger.info("Updating user: [{}]", username);
		
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
		
		boolean changed = false;
		
		if (dto.getFullName() != null && !dto.getFullName().isBlank()) {
			user.setFullName(dto.getFullName());
			changed = true;
		}
		
		if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
			if (userRepository.existsByEmailAndIdNot(dto.getEmail(), user.getId())) {
				logger.warn("Email already in use for update: [{}]", dto.getEmail());
				throw new IllegalArgumentException("Email already in use: " + dto.getEmail());
			}
			user.setEmail(dto.getEmail());
			changed = true;
		}
		
		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			user.setPassword(passwordEncoder.encode(dto.getPassword()));
			changed = true;
		}
		
		if (changed) {
			user.setUpdatedAt(LocalDateTime.now());
			User savedUser = userRepository.save(user);
			logger.info("User updated successfully: [{}]", username);
			
			List<String> roleNames = savedUser.getRoles().stream()
				.map(Role::getName)
				.collect(Collectors.toList());
			return new UserResponseDTO(
				savedUser.getId(),
				savedUser.getUsername(),
				savedUser.getFullName(),
				savedUser.getEmail(),
				roleNames
			);
		} else {
			logger.info("No fields provided for update for user [{}]", username);
			List<String> roleNames = user.getRoles().stream()
				.map(Role::getName)
				.collect(Collectors.toList());
			return new UserResponseDTO(
				user.getId(),
				user.getUsername(),
				user.getFullName(),
				user.getEmail(),
				roleNames
			);
		}
	}
	
	public UserResponseDTO findUser(String username) {
		logger.debug("Finding user: [{}]", username);
		
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> {
				logger.warn("User not found: [{}]", username);
				return new UsernameNotFoundException("User not found with username: " + username);
			});
		
		logger.debug("User found: [{}]", username);
		return mapper.entityToResponse(user);
	}
	
	public void deleteUser(String username) {
		logger.info("Deleting user: [{}]", username);
		
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> {
				logger.warn("User not found for deletion: [{}]", username);
				return new UsernameNotFoundException("User not found with username: " + username);
			});
		user.setDeletedAt(LocalDateTime.now());
		userRepository.save(user);
		logger.info("User deleted successfully: [{}]", username);
	}
	
	public Page<UserResponseDTO> listUsers(String role, Pageable pageable) {
		logger.info("Listing users with pagination: page={}, size={}, sort={}",
			pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		
		if (role != null && !role.isBlank()) {
			logger.info("Filtering users by role: [{}]", role);
		}
		
		Page<User> users;
		if (role != null && !role.isBlank()) {
			users = userRepository.findAllByRoles_Name(role, pageable);
		} else {
			users = userRepository.findAll(pageable);
		}
		
		logger.info("Found {} users{}",
			users.getNumberOfElements(),
			(role != null && !role.isBlank()) ? " with role " + role : "");
		
		return users.map(user -> {
			UserResponseDTO dto = mapper.entityToResponse(user);
			logger.debug("User mapped to DTO: {}", dto.getUsername());
			return dto;
		});
	}
	
	public void changePassword(String username, ChangePasswordRequestDTO dto) {
		logger.info("Changing password for user: [{}]", username);
		
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> {
				logger.warn("User not found for password change: [{}]", username);
				return new UsernameNotFoundException("User not found with username: " + username);
			});
		
		if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
			logger.warn("Invalid current password for user: [{}]", username);
			throw new InvalidCredentialsException("Current password is incorrect");
		}
		
		user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
		user.setUpdatedAt(LocalDateTime.now());
		userRepository.save(user);
		
		logger.info("Password changed successfully for user: [{}]", username);
	}
}