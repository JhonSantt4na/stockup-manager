package com.stockup.StockUp.Manager.dto.Stock.stock;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record StockRequestDTO(
	@NotNull String productId,
	@NotNull String warehouseId,
	@NotNull BigDecimal quantity,
	BigDecimal minimumQuantity,
	BigDecimal maximumQuantity
) {}
