package com.stockup.StockUp.Manager.service.finance;

import com.stockup.StockUp.Manager.dto.finance.payment.PaymentGatewayTransactionResponseDTO;

import java.util.UUID;

public interface IPaymentGatewayTransactionService {
	
	PaymentGatewayTransactionResponseDTO findPaymentGatewayTransactionByPayment(UUID paymentId);
}
