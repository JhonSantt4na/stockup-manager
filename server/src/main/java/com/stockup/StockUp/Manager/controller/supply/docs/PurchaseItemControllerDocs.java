package com.stockup.StockUp.Manager.controller.supply.docs;

import com.stockup.StockUp.Manager.dto.supply.PurchaseItem.PurchaseItemRequestDTO;
import com.stockup.StockUp.Manager.dto.supply.PurchaseItem.PurchaseItemResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Admin - PurchaseItem", description = "Gerenciamento de Itens de Compra")
public interface PurchaseItemControllerDocs {
	
	@Operation(summary = "Criar item de pedido")
	@PostMapping("/createCashMovement")
	ResponseEntity<PurchaseItemResponseDTO> createPurchaseItem(@RequestBody PurchaseItemRequestDTO dto);
	
	@Operation(summary = "Atualizar item de pedido")
	@PutMapping("/updatePaymentMethod/{id}")
	ResponseEntity<PurchaseItemResponseDTO> updatePurchaseItem(@PathVariable UUID id, @RequestBody PurchaseItemRequestDTO dto);
	
	@Operation(summary = "Buscar item por ID")
	@GetMapping("/{id}")
	ResponseEntity<PurchaseItemResponseDTO> findPurchaseItemById(@PathVariable UUID id);
	
	@Operation(summary = "Listar itens por pedido")
	@GetMapping("/order/{orderId}")
	ResponseEntity<List<PurchaseItemResponseDTO>> findItemsByOrder(@PathVariable UUID orderId);
	
	@Operation(summary = "Excluir item")
	@DeleteMapping("/deleteCashMovement/{id}")
	ResponseEntity<Void> deletePurchaseItem(@PathVariable UUID id);
}