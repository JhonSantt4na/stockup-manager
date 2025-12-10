package com.stockup.StockUp.Manager.service.finance;


import com.stockup.StockUp.Manager.dto.finance.payable.PayableRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payable.PayableResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IPayableService {
	
	PayableResponseDTO create(PayableRequestDTO dto);
	PayableResponseDTO update(UUID id, PayableRequestDTO dto);
	PayableResponseDTO findById(UUID id);
	Page<PayableResponseDTO> findAll(Pageable pageable);
	void delete(UUID id);
	List<PayableResponseDTO> findByPayment(UUID paymentId);
	List<PayableResponseDTO> findByStatus(String status);
	
}