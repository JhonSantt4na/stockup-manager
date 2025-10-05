package com.stockup.StockUp.Manager.dto.roles;

import java.util.UUID;

public record RoleResponseDTO(
	UUID id,
	String name,
	boolean enabled
) {}