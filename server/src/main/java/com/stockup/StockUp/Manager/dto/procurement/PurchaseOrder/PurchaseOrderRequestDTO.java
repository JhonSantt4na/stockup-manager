package com.stockup.StockUp.Manager.dto.procurement.PurchaseOrder;

import com.stockup.StockUp.Manager.dto.procurement.PurchaseItem.PurchaseItemRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record PurchaseOrderRequestDTO(
	
	@NotBlank(message = "O fornecedor é obrigatório.")
	String supplierId,
	
	@NotBlank(message = "O número do pedido é obrigatório.")
	String orderNumber,
	
	LocalDate expectedArrivalDate,
	
	@NotNull(message = "Itens do pedido são obrigatórios.")
	List<PurchaseItemRequestDTO> items
) {}
