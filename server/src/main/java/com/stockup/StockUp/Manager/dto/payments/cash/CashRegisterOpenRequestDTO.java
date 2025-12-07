package com.stockup.StockUp.Manager.dto.payments.cash;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CashRegisterOpenRequestDTO(
	
	@NotNull(message = "O valor inicial é obrigatório.")
	@PositiveOrZero(message = "O valor inicial não pode ser negativo.")
	BigDecimal openingAmount

) {}