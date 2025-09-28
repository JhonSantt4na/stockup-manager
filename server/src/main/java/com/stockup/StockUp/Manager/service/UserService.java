package com.stockup.StockUp.Manager.service;

import com.stockup.StockUp.Manager.StartUp;
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
import java.util.List;

@Service
public class UserService implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(StartUp.class);
	private static final Logger auditLogger = LoggerFactory.getLogger("audit");
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PermissionRepository permissionRepository;
	
	@Autowired
	UserMapper mapper;
	
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
	
	//Inconsistência leve para Corrigir:
	// user.getPermissions().addAll(rolesToAssign);
	// -> pode duplicar roles se chamar várias vezes.
	// -> usar Set<Permission> no User ou checar se a role já existe antes de adicionar.
	public UserResponseDTO assignRoles(String username, List<String> roleNames) {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
		List<Permission> rolesToAssign = permissionRepository.findAllByDescriptionIn(roleNames);
		
		if (rolesToAssign.isEmpty()) {
			throw new IllegalArgumentException("No valid roles found to assign");
		}
		
		user.getPermissions().addAll(rolesToAssign);
		User updatedUser = userRepository.save(user);
		
		return mapper.entityToResponse(updatedUser);
	}
	
	public List<String> getUserRoles(String username) {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return user.getRoles();
	}
	
	public UserResponseDTO registerUser(RegisterRequestDTO credentials){
		
		if (userRepository.existsByUsername(credentials.getUsername())) {
			throw new UsernameAlreadyExistsException("Username already exists: " + credentials.getUsername());
		}
		
		User newUser = mapper.registerToUser(credentials);
		newUser.setPassword(passwordEncoder.encode(credentials.getPassword()));
		newUser.setCreatedAt(LocalDateTime.now());
		
		User userSaved = userRepository.save(newUser);
		return mapper.entityToResponse(userSaved);
	}
	
	public UserResponseDTO updatedUser(RegisterRequestDTO credentials) {
		User user = userRepository.findByUsername(credentials.getUsername())
			.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + credentials.getUsername()));
		
		user.setFullName(credentials.getFullName());
		user.setEmail(credentials.getEmail());
		user.setPassword(passwordEncoder.encode(credentials.getPassword()));
		user.setUpdatedAt(LocalDateTime.now());
		
		User updatedUser = userRepository.save(user);
		return mapper.entityToResponse(updatedUser);
	}
	
	public UserResponseDTO findUser(String username){
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
		return mapper.entityToResponse(user);
	}
	
	public void deleteUser(String username) {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
		user.setDeletedAt(LocalDateTime.now());
		userRepository.save(user);
	}
	
	public void changePassword(String username, ChangePasswordRequestDTO dto) {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
		
		if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
			throw new InvalidCredentialsException("Current password is incorrect");
		}
		
		user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
		user.setUpdatedAt(LocalDateTime.now());
		userRepository.save(user);
	}
}