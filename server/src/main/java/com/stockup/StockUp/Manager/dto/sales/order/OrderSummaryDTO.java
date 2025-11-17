package com.stockup.StockUp.Manager.dto.sales.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderSummaryDTO(
	UUID id,
	UUID customerId,
	BigDecimal totalAmount,
	String status,
	LocalDateTime createdAt
) {}
