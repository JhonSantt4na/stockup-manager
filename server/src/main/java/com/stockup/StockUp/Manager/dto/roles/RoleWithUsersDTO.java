package com.stockup.StockUp.Manager.dto.roles;

import java.util.List;
import java.util.UUID;

public record RoleWithUsersDTO(
	UUID id,
	String name,
	boolean enabled,
	List<String> strings, List<UserInRoleDTO> users
) {}