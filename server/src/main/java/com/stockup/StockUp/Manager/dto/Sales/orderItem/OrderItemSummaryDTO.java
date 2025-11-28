package com.stockup.StockUp.Manager.dto.Sales.orderItem;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemSummaryDTO(
	
	UUID productId,
	Integer quantity,
	BigDecimal subtotal

) {}