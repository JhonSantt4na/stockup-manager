package com.stockup.StockUp.Manager.dto.payments.payment;

import java.util.UUID;

public record PaymentMethodResponseDTO(
	
	UUID id,
	String name,
	String gatewayType,
	Boolean active

) {}