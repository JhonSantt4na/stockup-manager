package com.stockup.StockUp.Manager.dto.permission.response;

import java.util.List;
import java.util.UUID;

public record PermissionWithRolesDTO(
	UUID id,
	String description,
	boolean enabled,
	List<RoleInPermissionDTO> roles
) {}