package com.stockup.StockUp.Manager.service.finance;

import com.stockup.StockUp.Manager.dto.finance.payment.PaymentMethodRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payment.PaymentMethodResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IPaymentMethodService {
	
	PaymentMethodResponseDTO create(PaymentMethodRequestDTO dto);
	PaymentMethodResponseDTO update(UUID id, PaymentMethodRequestDTO dto);
	PaymentMethodResponseDTO findById(UUID id);
	Page<PaymentMethodResponseDTO> findAll(Pageable pageable);
	void delete(UUID id);
	void activate(UUID id);
	void deactivate(UUID id);
}