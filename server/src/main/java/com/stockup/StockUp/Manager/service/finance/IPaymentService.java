package com.stockup.StockUp.Manager.service.finance;


import com.stockup.StockUp.Manager.dto.finance.payment.PaymentRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payment.PaymentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IPaymentService {
	
	PaymentResponseDTO create(PaymentRequestDTO dto);
	PaymentResponseDTO update(UUID id, PaymentRequestDTO dto);
	PaymentResponseDTO findById(UUID id);
	Page<PaymentResponseDTO> findAll(Pageable pageable);
	void delete(UUID id);
	PaymentResponseDTO updateStatus(UUID id, String status);
	List<PaymentResponseDTO> findByOrder(UUID orderId);
	List<PaymentResponseDTO> findByStatus(String status);
}