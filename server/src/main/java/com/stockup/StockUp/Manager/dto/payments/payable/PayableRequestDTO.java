package com.stockup.StockUp.Manager.dto.payments.payable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PayableRequestDTO(
	
	UUID supplierId,
	
	@NotNull(message = "O valor é obrigatório.")
	@Positive(message = "O valor deve ser positivo.")
	BigDecimal amount,
	
	@NotNull(message = "A data de emissão é obrigatória.")
	LocalDate issueDate,
	
	@NotNull(message = "A data de vencimento é obrigatória.")
	LocalDate dueDate,
	
	@NotNull(message = "A categoria é obrigatória.")
	String category,
	
	String description

) {}
