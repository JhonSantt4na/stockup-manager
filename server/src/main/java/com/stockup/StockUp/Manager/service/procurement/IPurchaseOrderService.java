package com.stockup.StockUp.Manager.service.procurement;

import com.stockup.StockUp.Manager.dto.procurement.PurchaseOrder.PurchaseOrderRequestDTO;
import com.stockup.StockUp.Manager.dto.procurement.PurchaseOrder.PurchaseOrderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface IPurchaseOrderService {
	
	PurchaseOrderResponseDTO create(PurchaseOrderRequestDTO dto);
	
	PurchaseOrderResponseDTO update(UUID id, PurchaseOrderRequestDTO dto);
	
	void delete(UUID id);
	
	PurchaseOrderResponseDTO findById(UUID id);
	
	List<PurchaseOrderResponseDTO> findAll();
	
	Page<PurchaseOrderResponseDTO> list(Pageable pageable);
}