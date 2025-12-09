package com.stockup.StockUp.Manager.service.finance;

import com.stockup.StockUp.Manager.dto.payments.payment.PaymentMethodRequestDTO;
import com.stockup.StockUp.Manager.dto.payments.payment.PaymentMethodResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IPaymentMethodService {
	
	PaymentMethodResponseDTO create(PaymentMethodRequestDTO dto);
	
	PaymentMethodResponseDTO update(UUID id, PaymentMethodRequestDTO dto);
	
	void activate(UUID id);
	
	void deactivate(UUID id);
	
	PaymentMethodResponseDTO findById(UUID id);
	
	List<PaymentMethodResponseDTO> findAll();
}