package com.stockup.StockUp.Manager.dto.sales.Brand;

import java.util.UUID;

public record BrandResponseDTO(
	UUID id,
	String name
) {}