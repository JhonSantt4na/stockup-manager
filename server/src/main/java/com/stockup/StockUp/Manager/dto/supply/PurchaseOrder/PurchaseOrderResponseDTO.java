package com.stockup.StockUp.Manager.dto.supply.PurchaseOrder;

import com.stockup.StockUp.Manager.Enums.pocurement.PurchaseOrderStatus;
import com.stockup.StockUp.Manager.dto.supply.PurchaseItem.PurchaseItemResponseDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PurchaseOrderResponseDTO(
	UUID id,
	String orderNumber,
	LocalDate expectedArrivalDate,
	PurchaseOrderStatus status,
	BigDecimal total,
	UUID supplierId,
	String supplierName,
	List<PurchaseItemResponseDTO> items
) {}