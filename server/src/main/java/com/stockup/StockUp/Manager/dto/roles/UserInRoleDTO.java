package com.stockup.StockUp.Manager.dto.roles;

import java.util.UUID;

public record UserInRoleDTO(
	UUID id,
	String username,
	String fullName
) {}