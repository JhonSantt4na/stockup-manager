package com.stockup.StockUp.Manager.dto.sales.order;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderSummaryDTO(
	
	UUID id,
	UUID customerId,
	BigDecimal totalAmount,
	String status,
	Instant createdAt

) {}