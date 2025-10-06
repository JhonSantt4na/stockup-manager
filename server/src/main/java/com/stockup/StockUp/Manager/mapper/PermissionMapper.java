package com.stockup.StockUp.Manager.mapper;

import com.stockup.StockUp.Manager.dto.permission.request.PermissionCreateDTO;
import com.stockup.StockUp.Manager.dto.permission.request.PermissionUpdateDTO;
import com.stockup.StockUp.Manager.model.security.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PermissionMapper {
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	Permission fromCreateDTO(PermissionCreateDTO dto);
	
	@Mapping(target = "description", source = "description")
	PermissionCreateDTO toCreateDTO(Permission permission);
	
	default Permission fromUpdateDTO(PermissionUpdateDTO dto) {
		Permission permission = new Permission();
		permission.setDescription(dto.getNewDescription());
		return permission;
	}
}