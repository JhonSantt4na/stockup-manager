package com.stockup.StockUp.Manager.dto.Stock.stock;

import com.stockup.StockUp.Manager.Enums.Stock.MovementReason;
import com.stockup.StockUp.Manager.Enums.Stock.MovementType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record StockMovementRequestDTO(
	@NotNull String productId,
	@NotNull String warehouseId,
	@NotNull MovementType movementType,
	@NotNull MovementReason reason,
	@NotNull BigDecimal quantity,
	String relatedDocumentId
) {}
