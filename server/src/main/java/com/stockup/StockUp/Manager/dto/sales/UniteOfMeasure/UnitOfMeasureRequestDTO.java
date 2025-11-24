package com.stockup.StockUp.Manager.dto.sales.UniteOfMeasure;

import jakarta.validation.constraints.NotBlank;

public record UnitOfMeasureRequestDTO(
	@NotBlank(message = "A abreviação é obrigatória.")
	String abbreviation,
	
	@NotBlank(message = "A descrição é obrigatória.")
	String description
) {}