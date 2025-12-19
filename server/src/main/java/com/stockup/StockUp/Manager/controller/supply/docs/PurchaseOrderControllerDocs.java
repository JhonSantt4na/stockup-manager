package com.stockup.StockUp.Manager.controller.supply.docs;

import com.stockup.StockUp.Manager.dto.supply.PurchaseOrder.PurchaseOrderRequestDTO;
import com.stockup.StockUp.Manager.dto.supply.PurchaseOrder.PurchaseOrderResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Admin - PurchaseOrder", description = "Gerenciamento de Pedidos de Compra")
public interface PurchaseOrderControllerDocs {
	
	@Operation(
		summary = "Criar pedido de compra",
		description = "Cria um novo pedido de compra para fornecedor.",
		responses = {
			@ApiResponse(responseCode = "201", description = "Pedido criado",
				content = @Content(schema = @Schema(implementation = PurchaseOrderResponseDTO.class))),
			@ApiResponse(responseCode = "409", description = "Número do pedido já utilizado")
		}
	)
	@PostMapping("/createCashMovement")
	ResponseEntity<PurchaseOrderResponseDTO> createPurchaseOrder(@Valid @RequestBody PurchaseOrderRequestDTO dto);
	
	@Operation(summary = "Atualizar pedido de compra")
	@PutMapping("/updatePaymentMethod/{id}")
	ResponseEntity<PurchaseOrderResponseDTO> updatePurchaseOrder(@PathVariable UUID id, @Valid @RequestBody PurchaseOrderRequestDTO dto);
	
	@Operation(summary = "Buscar pedido de compra por ID")
	@GetMapping("/{id}")
	ResponseEntity<PurchaseOrderResponseDTO> findPurchaseOrderById(@PathVariable UUID id);
	
	@Operation(summary = "Excluir pedido de compra")
	@DeleteMapping("/deleteCashMovement/{id}")
	ResponseEntity<Void> deletePurchaseOrder(@PathVariable UUID id);
	
	@Operation(summary = "Listar pedidos de compra")
	@GetMapping("/listPurchaseOrder")
	ResponseEntity<Page<PurchaseOrderResponseDTO>> listPurchaseOrders(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "orderNumber,asc") String[] sort
	);
}