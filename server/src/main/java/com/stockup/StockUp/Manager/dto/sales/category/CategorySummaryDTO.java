package com.stockup.StockUp.Manager.dto.sales.category;

import java.util.UUID;

public record CategorySummaryDTO(
	UUID id,
	String name,
	boolean enabled
) {}