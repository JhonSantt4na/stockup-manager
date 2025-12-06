package com.stockup.StockUp.Manager.dto.procurement.PurchaseItem;

import java.math.BigDecimal;
import java.util.UUID;

public record PurchaseItemResponseDTO(
	UUID id,
	UUID productId,
	String productName,
	Integer quantity,
	BigDecimal costPrice,
	BigDecimal subtotal
) {}

