package com.stockup.StockUp.Manager.controller.finance.docs;

import com.stockup.StockUp.Manager.dto.finance.payable.PayableRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payable.PayableResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@Tag(name = "Payables", description = "Contas a pagar")
public interface PayableControllerDocs {
	
	PayableResponseDTO create(PayableRequestDTO dto);
	
	PayableResponseDTO findById(UUID id);
	
	List<PayableResponseDTO> findAll();
	
	List<PayableResponseDTO> findBySupplier(UUID supplierId);
}
