package com.stockup.StockUp.Manager.service.finance;

import com.stockup.StockUp.Manager.dto.payments.payable.PayableRequestDTO;
import com.stockup.StockUp.Manager.dto.payments.payable.PayableResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IPayableService {
	
	PayableResponseDTO create(PayableRequestDTO dto);
	
	PayableResponseDTO findById(UUID id);
	
	List<PayableResponseDTO> findByPayment(UUID paymentId);
	
	List<PayableResponseDTO> findByStatus(String status);
	
	void updateStatus(UUID payableId, String newStatus);
}