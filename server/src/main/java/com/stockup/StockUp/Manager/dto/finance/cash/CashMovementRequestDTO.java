package com.stockup.StockUp.Manager.dto.finance.cash;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CashMovementRequestDTO(
	
	@NotNull(message = "O ID da sessão de caixa é obrigatório.")
	UUID sessionId,
	
	@NotNull(message = "O valor da movimentação é obrigatório.")
	@Positive(message = "O valor deve ser positivo.")
	BigDecimal amount,
	
	@NotNull(message = "O tipo da movimentação é obrigatório.")
	String type,
	
	String description

) {}
