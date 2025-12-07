package com.stockup.StockUp.Manager.dto.payments.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponseDTO(
	
	UUID id,
	UUID referenceId,
	BigDecimal amount,
	String status,
	String description,
	LocalDateTime paidAt,
	PaymentMethodResponseDTO method,
	PaymentGatewayTransactionResponseDTO gateway

) {}