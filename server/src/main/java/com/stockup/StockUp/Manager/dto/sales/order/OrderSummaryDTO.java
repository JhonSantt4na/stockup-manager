package com.stockup.StockUp.Manager.dto.sales.order;

import com.stockup.StockUp.Manager.Enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderSummaryDTO(
	
	UUID id,
	UUID customerId,
	BigDecimal totalAmount,
	OrderStatus status,
	LocalDateTime createdAt

) {}
