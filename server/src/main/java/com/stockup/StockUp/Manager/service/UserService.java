package com.stockup.StockUp.Manager.service;

import com.stockup.StockUp.Manager.dto.ChangePasswordRequestDTO;
import com.stockup.StockUp.Manager.dto.security.request.RegisterRequestDTO;
import com.stockup.StockUp.Manager.dto.security.response.UserResponseDTO;
import com.stockup.StockUp.Manager.exception.InvalidCredentialsException;
import com.stockup.StockUp.Manager.exception.UsernameAlreadyExistsException;
import com.stockup.StockUp.Manager.mapper.UserMapper;
import com.stockup.StockUp.Manager.model.User;
import com.stockup.StockUp.Manager.model.security.Permission;
import com.stockup.StockUp.Manager.repository.PermissionRepository;
import com.stockup.StockUp.Manager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserService implements UserDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PermissionRepository permissionRepository;
	
	@Autowired
	private UserMapper mapper;
	
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
			.orElseThrow(() -> {
				logger.warn("User not found: [{}]", username);
				return new UsernameNotFoundException("User not found: " + username);
			});
		
		List<Permission> rolesToAssign = permissionRepository.findAllByDescriptionIn(roleNames);
		
		if (rolesToAssign.isEmpty()) {
			logger.warn("No valid roles found to assign for user [{}]", username);
			throw new IllegalArgumentException("No valid roles found to assign");
		}
		
		Set<Permission> currentRoles = new HashSet<>(user.getPermissions());
		currentRoles.addAll(rolesToAssign);
		user.setPermissions(new ArrayList<>(currentRoles));
		
		User updatedUser = userRepository.save(user);
		logger.info("Assigned {} roles to user [{}]", rolesToAssign.size(), username);
		
		return mapper.entityToResponse(updatedUser);
	}
	
	public UserResponseDTO removeRoles(String username, List<String> roleNames) {
		logger.info("Removing roles from user [{}]: {}", username, roleNames);
		
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> {
				logger.warn("User not found: [{}]", username);
				return new UsernameNotFoundException("User not found: " + username);
			});
		
		List<String> existingRoles = user.getPermissions().stream()
			.map(Permission::getDescription)
			.toList();
		
		List<String> rolesToRemove = roleNames.stream()
			.filter(existingRoles::contains)
			.toList();
		
		if (rolesToRemove.isEmpty()) {
			logger.warn("No valid roles to remove from user [{}]", username);
			throw new IllegalArgumentException("No valid roles to remove");
		}
		
		user.getPermissions().removeIf(role -> rolesToRemove.contains(role.getDescription()));
		User savedUser = userRepository.save(user);
		
		logger.info("Removed {} roles from user [{}]", rolesToRemove.size(), username);
		return mapper.entityToResponse(savedUser);
	}
	
	public List<String> getUserRoles(String username) {
		logger.debug("Getting roles for user: [{}]", username);
		
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> {
				logger.warn("User not found: [{}]", username);
				return new UsernameNotFoundException("User not found: " + username);
			});
		
		List<String> roles = user.getPermissions().stream()
			.map(Permission::getDescription)
			.collect(Collectors.toList());
		
		logger.debug("User [{}] has {} roles", username, roles.size());
		return roles;
	}
	
	public UserResponseDTO registerUser(RegisterRequestDTO credentials) {
		logger.info("Registering new user: [{}]", credentials.getUsername());
		
		if (userRepository.existsByUsername(credentials.getUsername())) {
			logger.warn("Attempt to register existing username: [{}]", credentials.getUsername());
			throw new UsernameAlreadyExistsException("Username already exists: " + credentials.getUsername());
		}
		
		User newUser = mapper.registerToUser(credentials);
		newUser.setPassword(passwordEncoder.encode(credentials.getPassword()));
		newUser.setCreatedAt(LocalDateTime.now());
		
		User userSaved = userRepository.save(newUser);
		logger.info("User registered successfully: [{}]", credentials.getUsername());
		
		return mapper.entityToResponse(userSaved);
	}
	
	public UserResponseDTO updatedUser(RegisterRequestDTO credentials) {
		logger.info("Updating user: [{}]", credentials.getUsername());
		
		User user = userRepository.findByUsername(credentials.getUsername())
			.orElseThrow(() -> {
				logger.warn("User not found for update: [{}]", credentials.getUsername());
				return new UsernameNotFoundException("User not found with username: " + credentials.getUsername());
			});
		
		user.setFullName(credentials.getFullName());
		user.setEmail(credentials.getEmail());
		user.setPassword(passwordEncoder.encode(credentials.getPassword()));
		user.setUpdatedAt(LocalDateTime.now());
		
		User updatedUser = userRepository.save(user);
		logger.info("User updated successfully: [{}]", credentials.getUsername());
		
		return mapper.entityToResponse(updatedUser);
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