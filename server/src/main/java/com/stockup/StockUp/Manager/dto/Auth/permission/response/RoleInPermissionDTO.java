package com.stockup.StockUp.Manager.dto.Auth.permission.response;

import java.util.UUID;

public record RoleInPermissionDTO(
	UUID id,
	String name
) {}

