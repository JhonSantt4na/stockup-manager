package com.stockup.StockUp.Manager.mapper;

import com.stockup.StockUp.Manager.dto.security.request.RegisterRequestDTO;
import com.stockup.StockUp.Manager.dto.security.response.UserResponseDTO;
import com.stockup.StockUp.Manager.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "permissions", ignore = true) // será atribuído depois
	@Mapping(target = "lastActivity", ignore = true)
	@Mapping(target = "accountNonExpired", ignore = true)
	@Mapping(target = "accountNonLocked", ignore = true)
	@Mapping(target = "credentialsNonExpired", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	User registerToUser(RegisterRequestDTO dto);
	
	// User -> UserResponseDTO
	@Mapping(target = "roles", expression = "java(entity.getRoles())")
	// Remova os ignores abaixo se quiser mapear fullName e email automaticamente
	// @Mapping(target = "fullName", ignore = true) // Remova se quiser mapear
	// @Mapping(target = "email", ignore = true)    // Remova se quiser mapear
	UserResponseDTO entityToResponse(User entity);
	
	// UserResponseDTO -> User (se realmente precisar)
	@Mapping(target = "roles", ignore = true) // Ignora roles do DTO, pois não existe no User
	@Mapping(target = "permissions", ignore = true)
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
	
	// User -> RegisterRequestDTO
	@Mapping(target = "username", source = "username")
	@Mapping(target = "fullName", source = "fullName")
	@Mapping(target = "email", source = "email")
	@Mapping(target = "password", ignore = true)
	RegisterRequestDTO entityToRegister(User entity);
}