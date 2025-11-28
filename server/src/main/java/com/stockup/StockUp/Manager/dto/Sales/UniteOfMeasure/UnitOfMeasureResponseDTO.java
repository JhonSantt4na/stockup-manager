package com.stockup.StockUp.Manager.dto.Sales.UniteOfMeasure;

import java.util.UUID;

public record UnitOfMeasureResponseDTO(
	UUID id,
	String abbreviation,
	String description
) {}