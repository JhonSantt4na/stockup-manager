package com.stockup.StockUp.Manager.service.finance;


import com.stockup.StockUp.Manager.dto.finance.payment.PaymentRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payment.PaymentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IPaymentService {
	
	PaymentResponseDTO createPayment(PaymentRequestDTO dto);
	PaymentResponseDTO updatePayment(UUID id, PaymentRequestDTO dto);
	PaymentResponseDTO findPaymentById(UUID id);
	Page<PaymentResponseDTO> findAllPayment(Pageable pageable);
	void deletePayment(UUID id);
	PaymentResponseDTO updatePaymentStatus(UUID id, String status);
	List<PaymentResponseDTO> findPaymentByOrder(UUID orderId);
	List<PaymentResponseDTO> findPaymentByStatus(String status);
}