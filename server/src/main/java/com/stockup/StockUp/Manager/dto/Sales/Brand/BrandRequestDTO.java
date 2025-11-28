package com.stockup.StockUp.Manager.dto.Sales.Brand;

import jakarta.validation.constraints.NotBlank;

public record BrandRequestDTO(
	@NotBlank(message = "O nome é obrigatório.")
	String name
) {}