package com.stockup.StockUp.Manager.dto.sales.orderItem;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDTO(
	
	UUID productId,
	String productName,
	BigDecimal price,
	Integer quantity,
	BigDecimal subtotal

) {}