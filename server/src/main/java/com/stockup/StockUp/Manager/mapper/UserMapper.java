package com.stockup.StockUp.Manager.mapper;

import com.stockup.StockUp.Manager.dto.security.request.RegisterRequestDTO;
import com.stockup.StockUp.Manager.dto.security.response.UserResponseDTO;
import com.stockup.StockUp.Manager.model.User;
import com.stockup.StockUp.Manager.model.security.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
	
	// Mapping para RegisterRequestDTO -> User
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "lastActivity", ignore = true)
	@Mapping(target = "accountNonExpired", ignore = true)
	@Mapping(target = "accountNonLocked", ignore = true)
	@Mapping(target = "credentialsNonExpired", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	User registerToUser(RegisterRequestDTO dto);
	
	// Mapping para User -> UserResponseDTO
	@Mapping(target = "roles", expression = "java(mapRolesToNames(user))")
	UserResponseDTO entityToResponse(User user);
	
	// Mapping para UserResponseDTO -> User
	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "lastActivity", ignore = true)
	@Mapping(target = "accountNonExpired", ignore = true)
	@Mapping(target = "accountNonLocked", ignore = true)
	@Mapping(target = "credentialsNonExpired", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	@Mapping(target = "password", ignore = true)
	User responseToUser(UserResponseDTO dto);
	
	// Mapping User -> RegisterRequestDTO
	@Mapping(target = "username", source = "username")
	@Mapping(target = "fullName", source = "fullName")
	@Mapping(target = "email", source = "email")
	@Mapping(target = "password", ignore = true)
	RegisterRequestDTO entityToRegister(User user);
	
	// Helpers
	default List<String> mapRolesToNames(User user) {
		if (user.getRoles() == null) return new ArrayList<>();
		return user.getRoles().stream()
			.map(Role::getName)
			.collect(Collectors.toList());
	}
	
	default List<String> mapAuthorities(User user) {
		if (user.getAuthorities() == null) return new ArrayList<>();
		return user.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.toList());
	}
}