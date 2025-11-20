package com.stockup.StockUp.Manager.dto.sales.order;

import com.stockup.StockUp.Manager.dto.sales.orderItem.OrderItemRequestDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record OrderRequestDTO(
	
	@NotNull(message = "Customer ID is required.")
	UUID customerId,
	
	@NotNull(message = "Item list is required.")
	@Size(min = 1, message = "Order must contain at least one item.")
	List<OrderItemRequestDTO> items

) {}
