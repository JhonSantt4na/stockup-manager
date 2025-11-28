package com.stockup.StockUp.Manager.dto.Stock.warehouse;

import com.stockup.StockUp.Manager.Enums.Stock.WarehouseType;

public record WarehouseResponseDTO(
	String id,
	String name,
	String description,
	WarehouseType type,
	Boolean isDefault,
	String address
) {}
