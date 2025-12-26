package com.stockup.StockUp.Manager.service.procurement;

import com.stockup.StockUp.Manager.dto.supply.PurchaseOrder.PurchaseOrderRequestDTO;
import com.stockup.StockUp.Manager.dto.supply.PurchaseOrder.PurchaseOrderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface IPurchaseOrderService {
	PurchaseOrderResponseDTO createPurchaseOrder(PurchaseOrderRequestDTO dto);
	PurchaseOrderResponseDTO updatePurchaseOrder(UUID id, PurchaseOrderRequestDTO dto);
	void deletePurchaseOrder(UUID id);
	PurchaseOrderResponseDTO findPurchaseOrderById(UUID id);
	List<PurchaseOrderResponseDTO> findAllPurchaseOrder();
	Page<PurchaseOrderResponseDTO> listPurchaseOrder(Pageable pageable);
}