package com.stockup.StockUp.Manager.dto.sales.orderItem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemRequestDTO (
	
	@NotNull(message = "Product ID is required.")
	UUID productId,
	
	@Positive(message = "Quantity must be greater than zero.")
	Integer quantity,
	
	@NotNull(message = "Unit price is required.")
	@Positive(message = "Unit price must be greater than zero.")
	BigDecimal unitPrice

) {}
