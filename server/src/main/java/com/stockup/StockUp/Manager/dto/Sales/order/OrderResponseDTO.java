package com.stockup.StockUp.Manager.dto.Sales.order;

import com.stockup.StockUp.Manager.Enums.OrderStatus;
import com.stockup.StockUp.Manager.dto.Sales.Customer.CustomerSummaryDTO;
import com.stockup.StockUp.Manager.dto.Sales.orderItem.OrderItemResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(
	
	UUID id,
	CustomerSummaryDTO customer,
	BigDecimal totalAmount,
	OrderStatus status,
	LocalDateTime createdAt,
	LocalDateTime updatedAt,
	List<OrderItemResponseDTO> items

) {}
