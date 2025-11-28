package com.stockup.StockUp.Manager.dto.Stock.stock;

import com.stockup.StockUp.Manager.dto.Sales.Product.ProductResponseDTO;
import com.stockup.StockUp.Manager.dto.Stock.warehouse.WarehouseResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StockResponseDTO(
	String id,
	ProductResponseDTO product,
	WarehouseResponseDTO warehouse,
	BigDecimal quantity,
	BigDecimal reservedQuantity,
	BigDecimal minimumQuantity,
	BigDecimal maximumQuantity,
	BigDecimal lastCostPrice,
	BigDecimal averageCost,
	LocalDateTime updatedAt
) {}