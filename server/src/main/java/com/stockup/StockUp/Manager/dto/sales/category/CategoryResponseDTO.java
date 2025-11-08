package com.stockup.StockUp.Manager.dto.sales.category;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponseDTO(
	UUID id,
	String name,
	String description,
	LocalDateTime createdAt,
	LocalDateTime updatedAt,
	boolean enabled
) {}