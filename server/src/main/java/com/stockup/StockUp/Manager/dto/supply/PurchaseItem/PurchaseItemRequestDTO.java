package com.stockup.StockUp.Manager.dto.supply.PurchaseItem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PurchaseItemRequestDTO(
	
	@NotNull(message = "O produto é obrigatório.")
	String productId,
	
	@NotNull(message = "A quantidade é obrigatória.")
	@Positive(message = "A quantidade deve ser maior que zero.")
	Integer quantity,
	
	@NotNull(message = "O preço de custo é obrigatório.")
	@Positive(message = "O preço deve ser maior que zero.")
	String costPrice
) {}
