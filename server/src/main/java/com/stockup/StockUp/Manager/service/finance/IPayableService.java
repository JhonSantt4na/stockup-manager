package com.stockup.StockUp.Manager.service.finance;


import com.stockup.StockUp.Manager.dto.finance.payable.PayableRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payable.PayableResponseDTO;
import com.stockup.StockUp.Manager.model.finance.payable.Payable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IPayableService {
	PayableResponseDTO createPayable(PayableRequestDTO dto);
	PayableResponseDTO updatePayable(UUID id, PayableRequestDTO dto);
	PayableResponseDTO findPayableById(UUID id);
	Page<PayableResponseDTO> findAllPayable(Pageable pageable);
	void deletePayable(UUID id);
	List<PayableResponseDTO> findPayableByPayment(UUID paymentId);
	List<PayableResponseDTO> findPayableByStatus(String status);
	List<PayableResponseDTO> findPayableBySupplier(UUID supplierId);
}