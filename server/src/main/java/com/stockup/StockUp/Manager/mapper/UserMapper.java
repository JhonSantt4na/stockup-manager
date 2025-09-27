package com.stockup.StockUp.Manager.mapper;

import com.stockup.StockUp.Manager.dto.security.request.RegisterRequestDTO;
import com.stockup.StockUp.Manager.dto.security.response.UserResponseDTO;
import com.stockup.StockUp.Manager.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
	
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	@Mapping(target = "credentialsNonExpired", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "authorities", ignore = true)
	@Mapping(target = "accountNonLocked", ignore = true)
	@Mapping(target = "accountNonExpired", ignore = true)
	abstract User toEntity(UserResponseDTO dto);
	
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	@Mapping(target = "credentialsNonExpired", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "authorities", ignore = true)
	@Mapping(target = "accountNonLocked", ignore = true)
	@Mapping(target = "accountNonExpired", ignore = true)
	abstract User toEntity(RegisterRequestDTO registerDto);
	
}