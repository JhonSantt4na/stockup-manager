package com.stockup.StockUp.Manager.controller.procurement.docs;

import com.stockup.StockUp.Manager.dto.procurement.PurchaseItem.PurchaseItemRequestDTO;
import com.stockup.StockUp.Manager.dto.procurement.PurchaseItem.PurchaseItemResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Admin - PurchaseItem", description = "Gerenciamento de Itens de Compra")
public interface PurchaseItemControllerDocs {
	
	@Operation(summary = "Criar item de pedido")
	@PostMapping("/create")
	ResponseEntity<PurchaseItemResponseDTO> createPurchaseItem(@RequestBody PurchaseItemRequestDTO dto);
	
	@Operation(summary = "Atualizar item de pedido")
	@PutMapping("/update/{id}")
	ResponseEntity<PurchaseItemResponseDTO> updatePurchaseItem(@PathVariable UUID id, @RequestBody PurchaseItemRequestDTO dto);
	
	@Operation(summary = "Buscar item por ID")
	@GetMapping("/{id}")
	ResponseEntity<PurchaseItemResponseDTO> findPurchaseItemById(@PathVariable UUID id);
	
	@Operation(summary = "Listar itens por pedido")
	@GetMapping("/order/{orderId}")
	ResponseEntity<List<PurchaseItemResponseDTO>> findItemsByOrder(@PathVariable UUID orderId);
	
	@Operation(summary = "Excluir item")
	@DeleteMapping("/delete/{id}")
	ResponseEntity<Void> deletePurchaseItem(@PathVariable UUID id);
}