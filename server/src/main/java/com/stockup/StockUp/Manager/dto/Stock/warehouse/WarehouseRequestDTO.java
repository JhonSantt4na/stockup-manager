package com.stockup.StockUp.Manager.dto.Stock.warehouse;

import com.stockup.StockUp.Manager.Enums.Stock.WarehouseType;
import jakarta.validation.constraints.NotBlank;

public record WarehouseRequestDTO(
	@NotBlank(message = "O nome é obrigatório.")
	String name,
	String description,
	WarehouseType type,
	Boolean isDefault,
	String address
) {}