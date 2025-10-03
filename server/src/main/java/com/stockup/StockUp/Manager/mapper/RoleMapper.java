package com.stockup.StockUp.Manager.mapper;

import com.stockup.StockUp.Manager.dto.security.roles.RoleDTO;
import com.stockup.StockUp.Manager.model.security.Permission;
import com.stockup.StockUp.Manager.model.security.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {
	
	// Role -> RoleDTO (convertendo permissions para String)
	@Mapping(target = "permissions", expression = "java(mapPermissionsToNames(role))")
	RoleDTO toDTO(Role role);
	
	// RoleDTO -> Role (ignora lista de permissions, deve ser atribuída no service)
	@Mapping(target = "users", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	Role toEntity(RoleDTO dto);
	
	// Helper method
	default List<String> mapPermissionsToNames(Role role) {
		if (role.getPermissions() == null) return new ArrayList<>();
		return role.getPermissions().stream()
			.map(Permission::getDescription)
			.collect(Collectors.toList());
	}
}
