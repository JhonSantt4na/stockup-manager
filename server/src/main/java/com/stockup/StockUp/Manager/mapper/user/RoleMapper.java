package com.stockup.StockUp.Manager.mapper.user;

import com.stockup.StockUp.Manager.dto.Auth.roles.request.RoleDTO;
import com.stockup.StockUp.Manager.dto.Auth.roles.response.RoleWithUsersDTO;
import com.stockup.StockUp.Manager.dto.Auth.roles.response.UserInRoleDTO;
import com.stockup.StockUp.Manager.model.user.Permission;
import com.stockup.StockUp.Manager.model.user.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {
	
	@Mapping(target = "permissions", expression = "java(mapPermissionsToNames(role))")
	RoleDTO toDTO(Role role);
	
	@Mapping(target = "users", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	Role toEntity(RoleDTO dto);
	
	default List<String> mapPermissionsToNames(Role role) {
		if (role.getPermissions() == null) return new ArrayList<>();
		return role.getPermissions().stream()
			.map(Permission::getDescription)
			.collect(Collectors.toList());
	}
	
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