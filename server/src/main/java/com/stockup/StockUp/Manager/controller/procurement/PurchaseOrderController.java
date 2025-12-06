package com.stockup.StockUp.Manager.controller.procurement;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.procurement.docs.PurchaseOrderControllerDocs;
import com.stockup.StockUp.Manager.dto.procurement.PurchaseOrder.PurchaseOrderRequestDTO;
import com.stockup.StockUp.Manager.dto.procurement.PurchaseOrder.PurchaseOrderResponseDTO;
import com.stockup.StockUp.Manager.service.procurement.IPurchaseOrderService;
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
	@PostMapping("/create")
	public ResponseEntity<PurchaseOrderResponseDTO> createPurchaseOrder(@RequestBody PurchaseOrderRequestDTO dto) {
		PurchaseOrderResponseDTO response = service.create(dto);
		AuditLogger.log("PURCHASE_ORDER_CREATE", getCurrentUser(), "SUCCESS", "Order created: " + dto.orderNumber());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<PurchaseOrderResponseDTO> updatePurchaseOrder(@PathVariable UUID id, @RequestBody PurchaseOrderRequestDTO dto) {
		return ResponseEntity.ok(service.update(id, dto));
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<PurchaseOrderResponseDTO> findPurchaseOrderById(@PathVariable UUID id) {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deletePurchaseOrder(@PathVariable UUID id) {
		service.delete(id);
		AuditLogger.log("PURCHASE_ORDER_DELETE", getCurrentUser(), "SUCCESS", "Order deleted: " + id);
		return ResponseEntity.noContent().build();
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/list")
	public ResponseEntity<Page<PurchaseOrderResponseDTO>> listPurchaseOrders(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "orderNumber,asc") String[] sort
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.by(sort[0])));
		Page<PurchaseOrderResponseDTO> response = service.list(pageable);
		return ResponseEntity.ok(response);
	}
}
