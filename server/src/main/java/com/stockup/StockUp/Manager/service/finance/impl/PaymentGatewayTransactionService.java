package com.stockup.StockUp.Manager.service.finance.impl;

import com.stockup.StockUp.Manager.dto.finance.payment.PaymentGatewayTransactionResponseDTO;
import com.stockup.StockUp.Manager.mapper.finance.PaymentGatewayTransactionMapper;
import com.stockup.StockUp.Manager.model.finance.payments.PaymentGatewayTransaction;
import com.stockup.StockUp.Manager.repository.finance.PaymentGatewayTransactionRepository;
import com.stockup.StockUp.Manager.service.finance.IPaymentGatewayTransactionService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentGatewayTransactionService implements IPaymentGatewayTransactionService {
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentGatewayTransactionService.class);
	private final PaymentGatewayTransactionRepository repository;
	private final PaymentGatewayTransactionMapper mapper;
	
	@Override
	public PaymentGatewayTransactionResponseDTO findByPayment(UUID paymentId) {
		PaymentGatewayTransaction t = repository.findByPaymentId(paymentId)
			.orElseThrow(() -> new IllegalArgumentException("Transação de gateway não encontrada para paymentId: " + paymentId));
		return mapper.toResponse(t);
	}
}