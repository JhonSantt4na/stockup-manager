package com.stockup.StockUp.Manager.dto.sales.order;

import com.stockup.StockUp.Manager.dto.sales.orderItem.OrderItemResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(
	UUID id,
	UUID customerId,
	BigDecimal totalAmount,
	String status,
	LocalDateTime createdAt,
	LocalDateTime updatedAt,
	List<OrderItemResponseDTO> items
) {}
