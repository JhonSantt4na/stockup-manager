package com.stockup.StockUp.Manager.dto.Auth.roles.response;

import java.util.UUID;

public record RoleResponseDTO(
	UUID id,
	String name,
	boolean enabled
) {}