package com.stockup.StockUp.Manager.service.procurement;

import com.stockup.StockUp.Manager.dto.procurement.PurchaseItem.PurchaseItemRequestDTO;
import com.stockup.StockUp.Manager.dto.procurement.PurchaseItem.PurchaseItemResponseDTO;
import java.util.List;
import java.util.UUID;

public interface IPurchaseItemService {
	
	PurchaseItemResponseDTO create(PurchaseItemRequestDTO dto);
	
	PurchaseItemResponseDTO update(UUID id, PurchaseItemRequestDTO dto);
	
	void delete(UUID id);
	
	PurchaseItemResponseDTO findById(UUID id);
	
	List<PurchaseItemResponseDTO> findByPurchaseOrder(UUID purchaseOrderId);
}