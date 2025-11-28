package com.stockup.StockUp.Manager.mapper.user;

import com.stockup.StockUp.Manager.dto.Auth.user.request.RegisterUserRequestDTO;
import com.stockup.StockUp.Manager.dto.Auth.user.request.UpdateUserRequestDTO;
import com.stockup.StockUp.Manager.dto.Auth.user.response.UserResponseDTO;
import com.stockup.StockUp.Manager.model.user.User;
import com.stockup.StockUp.Manager.model.user.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
	
	@Mapping(target = "authorities", ignore = true)
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
	User registerToUser(RegisterUserRequestDTO dto);
	
	@Mapping(target = "authorities", ignore = true)
	@Mapping(target = "fullName", source = "fullName")
	@Mapping(target = "email", source = "email")
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "username", ignore = true)
	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "lastActivity", ignore = true)
	@Mapping(target = "accountNonExpired", ignore = true)
	@Mapping(target = "accountNonLocked", ignore = true)
	@Mapping(target = "credentialsNonExpired", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	User updateToUser(UpdateUserRequestDTO dto);
	
	@Mapping(target = "id", source = "id")
	@Mapping(target = "username", source = "username")
	@Mapping(target = "fullName", source = "fullName")
	@Mapping(target = "email", source = "email")
	@Mapping(target = "roles", expression = "java(mapRolesToNames(user))")
	UserResponseDTO entityToResponse(User user);
	
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "authorities", ignore = true)
	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "lastActivity", ignore = true)
	@Mapping(target = "accountNonExpired", ignore = true)
	@Mapping(target = "accountNonLocked", ignore = true)
	@Mapping(target = "credentialsNonExpired", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	User responseToUser(UserResponseDTO dto);
	
	@Mapping(target = "username", source = "username")
	@Mapping(target = "fullName", source = "fullName")
	@Mapping(target = "email", source = "email")
	RegisterUserRequestDTO entityToRegister(User user);
	
	default List<String> mapRolesToNames(User user) {
		if (user.getRoles() == null) return new ArrayList<>();
		return user.getRoles().stream()
			.map(Role::getName)
			.collect(Collectors.toList());
	}
	
	default List<String> mapAuthorities(User user) {
		if (user.getAuthorities() == null) return new ArrayList<>();
		return user.getAuthorities().stream()
			.map(g -> g.getAuthority())
			.collect(Collectors.toList());
	}
}