package com.stockup.StockUp.Manager.service.finance.impl;

import com.stockup.StockUp.Manager.dto.payments.payment.PaymentGatewayTransactionResponseDTO;
import com.stockup.StockUp.Manager.exception.NotFoundException;
import com.stockup.StockUp.Manager.model.finance.payments.PaymentGatewayTransaction;
import com.stockup.StockUp.Manager.service.finance.IPaymentGatewayTransactionService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentGatewayTransactionService implements IPaymentGatewayTransactionService {
	
	private final PaymentGatewayTransactionRepository repository;
	private final PaymentGatewayTransactionMapper mapper;
	
	@Override
	public PaymentGatewayTransactionResponseDTO findByPayment(UUID paymentId) {
		PaymentGatewayTransaction entity = repository.findByPaymentId(paymentId)
			.orElseThrow(() -> new NotFoundException(
				"Nenhuma transação de gateway encontrada para este pagamento."
			));
		
		return mapper.toDto(entity);
	}
}
