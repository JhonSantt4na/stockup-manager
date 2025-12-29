package com.stockup.StockUp.Manager.controller.finance.docs;

import com.stockup.StockUp.Manager.dto.finance.payable.PayableRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payable.PayableResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Payables", description = "Contas a pagar")
public interface PayableControllerDocs {
	ResponseEntity<PayableResponseDTO> createPayable(PayableRequestDTO dto);
	ResponseEntity<PayableResponseDTO> findPayableById(UUID id);
	ResponseEntity<Page<PayableResponseDTO>> findAllPayable(Pageable pageable);
	ResponseEntity<List<PayableResponseDTO>> findPayableBySupplier(UUID supplierId);
}
