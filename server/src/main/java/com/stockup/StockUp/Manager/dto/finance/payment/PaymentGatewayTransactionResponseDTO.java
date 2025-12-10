package com.stockup.StockUp.Manager.dto.finance.payment;

import java.math.BigDecimal;

public record PaymentGatewayTransactionResponseDTO(
	
	String externalTransactionId,
	String qrCode,
	String pixCopiaECola,
	String cardBrand,
	Integer installments,
	BigDecimal fees,
	BigDecimal netAmount

) {}