package com.stockup.StockUp.Manager.dto.Sales.Brand;

import java.util.UUID;

public record BrandResponseDTO(
	UUID id,
	String name
) {}