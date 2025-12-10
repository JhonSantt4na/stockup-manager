package com.stockup.StockUp.Manager.dto.finance.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequestDTO(
	
	@NotNull(message = "O valor do pagamento é obrigatório.")
	@Positive(message = "O valor deve ser maior que zero.")
	BigDecimal amount,
	
	@NotNull(message = "O método de pagamento é obrigatório.")
	UUID paymentMethodId,
	
	@NotNull(message = "O ID de referência é obrigatório.")
	UUID referenceId,
	
	String description

) {}