package com.stockup.StockUp.Manager.controller.supply;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.supply.docs.PurchaseOrderControllerDocs;
import com.stockup.StockUp.Manager.dto.supply.PurchaseOrder.PurchaseOrderRequestDTO;
import com.stockup.StockUp.Manager.dto.supply.PurchaseOrder.PurchaseOrderResponseDTO;
import com.stockup.StockUp.Manager.service.procurement.IPurchaseOrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/api/v1/supply/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController implements PurchaseOrderControllerDocs {
	
	private final IPurchaseOrderService service;
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/createCashMovement")
	public ResponseEntity<PurchaseOrderResponseDTO> createPurchaseOrder(@RequestBody @Valid PurchaseOrderRequestDTO dto) {
		PurchaseOrderResponseDTO response = service.createPurchaseOrder(dto);
		AuditLogger.log("PURCHASE_ORDER_CREATE", getCurrentUser(), "SUCCESS", "Order created: " + dto.orderNumber());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updatePaymentMethod/{id}")
	public ResponseEntity<PurchaseOrderResponseDTO> updatePurchaseOrder(@PathVariable @NotNull UUID id, @RequestBody @Valid PurchaseOrderRequestDTO dto) {
		return ResponseEntity.ok(service.updatePurchaseOrder(id, dto));
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<PurchaseOrderResponseDTO> findPurchaseOrderById(@PathVariable @NotNull UUID id) {
		return ResponseEntity.ok(service.findPurchaseOrderById(id));
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/deleteCashMovement/{id}")
	public ResponseEntity<Void> deletePurchaseOrder(@PathVariable @NotNull UUID id) {
		service.deletePurchaseOrder(id);
		AuditLogger.log("PURCHASE_ORDER_DELETE", getCurrentUser(), "SUCCESS", "Order deleted: " + id);
		return ResponseEntity.noContent().build();
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping()
	public ResponseEntity<Page<PurchaseOrderResponseDTO>> listPurchaseOrders(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "orderNumber,asc") String[] sort
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.by(sort[0])));
		Page<PurchaseOrderResponseDTO> response = service.listPurchaseOrder(pageable);
		return ResponseEntity.ok(response);
	}
}
