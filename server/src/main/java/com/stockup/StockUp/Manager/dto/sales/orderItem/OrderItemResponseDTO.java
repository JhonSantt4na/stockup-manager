package com.stockup.StockUp.Manager.dto.sales.orderItem;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDTO(
	
	UUID productId,
	String productName,
	BigDecimal unitPrice,
	BigDecimal finalPrice,
	Integer quantity,
	BigDecimal subtotal

) {}
