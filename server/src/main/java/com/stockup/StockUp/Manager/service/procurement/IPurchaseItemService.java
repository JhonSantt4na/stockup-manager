package com.stockup.StockUp.Manager.service.procurement;

import com.stockup.StockUp.Manager.dto.supply.PurchaseItem.PurchaseItemRequestDTO;
import com.stockup.StockUp.Manager.dto.supply.PurchaseItem.PurchaseItemResponseDTO;
import java.util.List;
import java.util.UUID;

public interface IPurchaseItemService {
	PurchaseItemResponseDTO createPurchaseItem(PurchaseItemRequestDTO dto);
	PurchaseItemResponseDTO updatePurchaseItem(UUID id, PurchaseItemRequestDTO dto);
	void deletePurchaseItem(UUID id);
	PurchaseItemResponseDTO findPurchaseItemById(UUID id);
	List<PurchaseItemResponseDTO> findPurchaseItemByPurchaseOrder(UUID purchaseOrderId);
}