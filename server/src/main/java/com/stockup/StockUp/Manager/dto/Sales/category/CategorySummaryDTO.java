package com.stockup.StockUp.Manager.dto.Sales.category;

import java.util.UUID;

public record CategorySummaryDTO(
	UUID id,
	String name,
	boolean enabled
) {}