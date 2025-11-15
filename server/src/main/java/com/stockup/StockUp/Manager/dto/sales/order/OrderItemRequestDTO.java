package com.stockup.StockUp.Manager.dto.sales.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record OrderItemRequestDTO(
	
	@NotNull(message = "Product ID is required.")
	UUID productId,
	
	@Positive(message = "Quantity must be greater than zero.")
	Integer quantity

) {}