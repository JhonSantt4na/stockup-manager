package com.stockup.StockUp.Manager.dto.payments.cash;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CashRegisterCloseRequestDTO(
	
	@NotNull(message = "O valor final informado é obrigatório.")
	@PositiveOrZero(message = "O valor final não pode ser negativo.")
	BigDecimal closingAmount

) {}