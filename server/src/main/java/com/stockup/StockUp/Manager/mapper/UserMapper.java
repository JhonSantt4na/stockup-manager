package com.stockup.StockUp.Manager.mapper;

import com.stockup.StockUp.Manager.dto.security.request.RegisterRequestDTO;
import com.stockup.StockUp.Manager.dto.security.response.UserResponseDTO;
import com.stockup.StockUp.Manager.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
	
	// DTO de registro -> User
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
	UserResponseDTO entityToResponse(User entity);
	
	// UserResponseDTO -> User (se realmente precisar)
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
	@Mapping(target = "password", ignore = true) // senha não deve ser retornada
	RegisterRequestDTO entityToRegister(User entity);
}
