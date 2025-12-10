package com.stockup.StockUp.Manager.dto.finance.cash;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CashEntryRequestDTO(
	
	@NotNull(message = "O tipo de movimentação é obrigatório.")
	String movementType,
	
	@NotNull(message = "O valor é obrigatório.")
	@Positive(message = "O valor deve ser maior que zero.")
	BigDecimal amount,
	
	String description,
	
	@NotNull(message = "O ID do caixa é obrigatório.")
	UUID cashRegisterId

) {}