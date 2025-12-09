package com.stockup.StockUp.Manager.service.finance;

import com.stockup.StockUp.Manager.dto.payments.payment.PaymentRequestDTO;
import com.stockup.StockUp.Manager.dto.payments.payment.PaymentResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IPaymentService {
	
	PaymentResponseDTO create(PaymentRequestDTO dto);
	
	PaymentResponseDTO updateStatus(UUID id, String status);
	
	PaymentResponseDTO findById(UUID id);
	
	List<PaymentResponseDTO> findByOrder(UUID orderId);
	
	List<PaymentResponseDTO> findByStatus(String status);
}