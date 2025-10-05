package com.stockup.StockUp.Manager.mapper;

import com.stockup.StockUp.Manager.dto.roles.RoleDTO;
import com.stockup.StockUp.Manager.dto.roles.RoleWithUsersDTO;
import com.stockup.StockUp.Manager.dto.roles.UserInRoleDTO;
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
	
	// Role -> RoleDTO (permissions para List<String>)
	@Mapping(target = "permissions", expression = "java(mapPermissionsToNames(role))")
	RoleDTO toDTO(Role role);
	
	// RoleDTO -> Role (ignora lista de permissions, deve ser atribuída no service)
	@Mapping(target = "users", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	Role toEntity(RoleDTO dto);
	
	// Helper para mapear permissions
	default List<String> mapPermissionsToNames(Role role) {
		if (role.getPermissions() == null) return new ArrayList<>();
		return role.getPermissions().stream()
			.map(Permission::getDescription)
			.collect(Collectors.toList());
	}
	
	// Role -> RoleWithUsersDTO (proteção contra null)
	default RoleWithUsersDTO toWithUsersDTO(Role role) {
		List<UserInRoleDTO> users;
		if (role.getUsers() == null) {
			users = new ArrayList<>();
		} else {
			users = role.getUsers().stream()
				.map(u -> new UserInRoleDTO(u.getId(), u.getUsername(), u.getFullName()))
				.collect(Collectors.toList());
		}
		
		List<String> permissions = role.getPermissions() == null
			? new ArrayList<>()
			: role.getPermissions().stream()
			.map(Permission::getDescription)
			.collect(Collectors.toList());
		
		return new RoleWithUsersDTO(
			role.getId(),
			role.getName(),
			role.isEnabled(),
			permissions,
			users
		);
	}
}