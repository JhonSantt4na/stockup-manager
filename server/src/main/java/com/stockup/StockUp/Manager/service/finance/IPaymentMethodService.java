package com.stockup.StockUp.Manager.service.finance;

import com.stockup.StockUp.Manager.dto.finance.payment.PaymentMethodRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payment.PaymentMethodResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IPaymentMethodService {
	PaymentMethodResponseDTO createPaymentMethod(PaymentMethodRequestDTO dto);
	PaymentMethodResponseDTO updatePaymentMethod(UUID id, PaymentMethodRequestDTO dto);
	PaymentMethodResponseDTO findPaymentMethodById(UUID id);
	Page<PaymentMethodResponseDTO> findAllPaymentMethod(Pageable pageable);
	void deletePaymentMethod(UUID id);
	void activatePaymentMethod(UUID id);
	void deactivatePaymentMethod(UUID id);
}