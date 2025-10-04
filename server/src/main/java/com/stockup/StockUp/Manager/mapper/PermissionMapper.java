package com.stockup.StockUp.Manager.mapper;

import com.stockup.StockUp.Manager.dto.permission.PermissionCreateDTO;
import com.stockup.StockUp.Manager.dto.permission.PermissionUpdateDTO;
import com.stockup.StockUp.Manager.model.security.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PermissionMapper {
	
	// CreateDTO -> Permission
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	Permission fromCreateDTO(PermissionCreateDTO dto);
	
	// Permission -> CreateDTO (opcional)
	@Mapping(target = "description", source = "description")
	PermissionCreateDTO toCreateDTO(Permission permission);
	
	// UpdateDTO
	default Permission fromUpdateDTO(PermissionUpdateDTO dto) {
		Permission permission = new Permission();
		permission.setDescription(dto.getNewDescription());
		return permission;
	}
}