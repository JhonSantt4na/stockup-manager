package com.stockup.StockUp.Manager.dto.Stock.stock;

import com.stockup.StockUp.Manager.Enums.Stock.MovementReason;
import com.stockup.StockUp.Manager.Enums.Stock.MovementType;
import com.stockup.StockUp.Manager.dto.Sales.Product.ProductResponseDTO;
import com.stockup.StockUp.Manager.dto.Stock.warehouse.WarehouseResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StockMovementResponseDTO(
	String id,
	ProductResponseDTO product,
	WarehouseResponseDTO warehouse,
	MovementType movementType,
	MovementReason reason,
	BigDecimal quantity,
	BigDecimal previousQuantity,
	BigDecimal finalQuantity,
	BigDecimal unitCost,
	BigDecimal totalCost,
	String relatedDocumentId,
	String operatorId,
	LocalDateTime timestamp
) {}