package com.stockup.StockUp.Manager.dto.finance.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentMethodRequestDTO(
	
	@NotBlank(message = "O nome do método de pagamento é obrigatório.")
	String name,
	
	@NotNull(message = "O tipo de gateway é obrigatório.")
	String gatewayType,
	
	Boolean active

) {}